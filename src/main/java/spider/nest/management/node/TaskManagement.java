package spider.nest.management.node;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spder.task.Task;
import spider.nest.common.TASK_STATE;
import spider.nest.management.exception.IllegalTaskStateException;
import spider.nest.management.node.watcher.TaskWatcher;
import spider.nest.util.ZNodePathUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class TaskManagement {
    private TaskWatcher taskWatcher;
    private CuratorFramework client;
    private ZNodeManagement zNodeManagement;
    private ZNodePathUtil zNodePathUtil;
    @Autowired
    public TaskManagement(TaskWatcher taskSubmitWatcher,CuratorFramework client
            , ZNodeManagement zNodeManagement, ZNodePathUtil zNodePathUtil) throws Exception {
        this.taskWatcher = taskSubmitWatcher;
        this.client = client;
        this.zNodeManagement = zNodeManagement;
        this.zNodePathUtil = zNodePathUtil;
    }
    public int getCurrentTaskNum() throws Exception {
        return taskWatcher.getCurrentTaskNum();
    }

    public void submitTask(Task task) throws Exception {
        task.setState(TASK_STATE.INIT.getState());
        zNodeManagement.createTaskNode(task,client);

    }

    public void closeTask(Task task) throws Exception {
        zNodeManagement.switchTaskNodeState(client,task.getUuId(),TASK_STATE.CLOSE);
        new asynCloseTaskThread(){
            @Override
            public void run() {
                long nanosTimeout = TimeUnit.SECONDS.toNanos(10);
                final long deadline = System.nanoTime() + nanosTimeout;
                for(;;){
                    try {
                        if(new String(zNodeManagement.getNodeData(client,zNodePathUtil.getSpecifiedTaskPath(task))).equals(TASK_STATE.CLOSE_SUCCESS.getState())){
                            zNodeManagement.deleteNode(client,zNodePathUtil.getSpecifiedTaskPath(task));
                            break;
                        }
                        nanosTimeout = deadline - System.nanoTime();
                        if (nanosTimeout <= 0L){
                            zNodeManagement.switchTaskNodeState(client,task.getUuId(),TASK_STATE.CLOSE_ERROR);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public List<Task> getAllTaskInfo() throws Exception {
        List<String> taskList = client.getChildren().forPath(zNodePathUtil.getTaskPath());
        List<Task> result = new LinkedList<>();
        for(String taskUuid : taskList){
            Task task = new Task();
            task.setUuId(taskUuid);
            task.setState(new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskPath(taskUuid))));
            JsonObject temp = new JsonParser().parse(new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskInfoPath(taskUuid)))).getAsJsonObject();
            task.setOwner(new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskOwnerPath(taskUuid))));
            task.setCompany(temp.get("company").toString().replace("\"",""));
            task.setTemplateName(temp.get("templateName").toString().replace("\"",""));
            result.add(task);
        }
        return result;
    }

    public Task getSpecifiedTaskInfo(Task task) throws Exception {
        String taskUuid = task.getUuId();
        Task result = new Task();
        result.setUuId(taskUuid);
        result.setState(new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskPath(taskUuid))));
        JsonObject temp = new JsonParser().parse(new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskInfoPath(taskUuid)))).getAsJsonObject();
        result.setOwner(new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskOwnerPath(taskUuid))));
        result.setCompany(temp.get("company").toString().replace("\"",""));
        result.setTemplateName(temp.get("templateName").toString().replace("\"",""));
        return result;
    }

//    public void deleteTask(Task task) throws Exception {
//        checkTaskState(task);
//        zNodeManagement.deleteNode(client,zNodePathUtil.getTaskPath());
//        taskWatcher.removeWatcher(task.getUuId());
//    }

    public void restartTask(Task task) throws Exception {
        checkTaskState(task);
        zNodeManagement.switchTaskNodeState(client,task.getUuId(),TASK_STATE.WAIT_FOR_RUN);
    }

    private void checkTaskState(Task task) throws Exception {
        if(new String(zNodeManagement.getNodeData(client,zNodePathUtil.getSpecifiedTaskPath(task))).equals("stop_error")
                ||new String(zNodeManagement.getNodeData(client,zNodePathUtil.getSpecifiedTaskPath(task))).equals("running_error")){

        }
        else{
            throw new IllegalTaskStateException("Task State is not stop_error or running_error");
        }
    }
}
