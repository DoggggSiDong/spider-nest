package spider.nest.management.node;

import com.google.gson.JsonObject;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spder.task.Task;
import spider.nest.common.TASK_STATE;
import spider.nest.management.balance.Balance;
import spider.nest.management.exception.NoActicveSpiderWebServerExcpetion;
import spider.nest.management.exception.TaskExistException;
import spider.nest.util.ZNodePathUtil;

@Component
public class ZNodeManagement {
    private SpiderWebManagement spiderWebManagement;
    private ZNodePathUtil zNodePathUtil;
    private Balance balance;
    @Autowired
    public ZNodeManagement(SpiderWebManagement spiderWebManagement, ZNodePathUtil zNodePathUtil, Balance balance) {
        this.spiderWebManagement = spiderWebManagement;
        this.zNodePathUtil = zNodePathUtil;
        this.balance = balance;
    }

    public void createTaskNode(Task task, CuratorFramework client) throws Exception {
        if (spiderWebManagement.getActiveNodeNum() <= 0) {
            throw new NoActicveSpiderWebServerExcpetion("No actve Spider Web Server");
        }
        if (client.checkExists().forPath(zNodePathUtil.getSpecifiedTaskPath(task)) != null) {
            throw new TaskExistException("Task exist");
        }
        JsonObject info = new JsonObject();
        String balanceOwner = client.getChildren().forPath(zNodePathUtil.getSpiderWebNodePath()).get(balance.next()-1);
        task.setOwner(balanceOwner);
        info.addProperty("company", task.getCompany());
        info.addProperty("templateName", task.getTemplateName());
        info.addProperty("period",task.getPeriod());
        client.transaction().forOperations(client.transactionOp().create().forPath(zNodePathUtil.getSpecifiedTaskPath(task), TASK_STATE.WAIT_FOR_RUN.getState().getBytes())
                , client.transactionOp().create().forPath(zNodePathUtil.getSpecifiedTaskOwnerPath(task), task.getOwner().getBytes())
                , client.transactionOp().create().forPath(zNodePathUtil.getSpecifiedTaskInfoPath(task), info.toString().getBytes())
        );

    }

    public void switchTaskNodeState(CuratorFramework client, String uuid, TASK_STATE taskState) throws Exception {
        client.setData().forPath(zNodePathUtil.getSpecifiedTaskPath(uuid), taskState.getState().getBytes());
    }

    public byte[] getNodeData(CuratorFramework client, String path) throws Exception {
        return client.getData().forPath(path);
    }

    public void deleteNode(CuratorFramework client, String path) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath(path);

    }
}
