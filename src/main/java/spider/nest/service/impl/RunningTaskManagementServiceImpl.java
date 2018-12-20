package spider.nest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spder.task.Task;
import spider.nest.management.node.TaskManagement;
import spider.nest.service.RunningTaskManagementService;

import java.util.List;

@Service
public class RunningTaskManagementServiceImpl implements RunningTaskManagementService {
    private TaskManagement taskManagement;
    @Autowired
    public RunningTaskManagementServiceImpl(TaskManagement taskManagement){
        this.taskManagement = taskManagement;
    }
    @Override
    public void submitTask(Task task) throws Exception {
        taskManagement.submitTask(task);
    }

    @Override
    public Object search() {
        return null;
    }

    @Override
    public Task get(Task task) throws Exception {
        return taskManagement.getSpecifiedTaskInfo(task);
    }

    @Override
    public List<Task> getAll() throws Exception {
        return taskManagement.getAllTaskInfo();
    }
    @Override
    public int getTaskNum() throws Exception {
        return taskManagement.getCurrentTaskNum();
    }
    @Override
    public void close(Task task) throws Exception {
        taskManagement.closeTask(task);
    }
//    @Override
//    public void delete(Task task) throws Exception {
//        taskManagement.deleteTask(task);
//    }
    @Override
    public void update() {

    }
    @Override
    public void restart(Task task) throws Exception {
        taskManagement.restartTask(task);
    }
}
