package spider.nest.management.policy;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.nest.common.TASK_STATE;
import spider.nest.management.balance.Balance;
import spider.nest.util.ZNodePathUtil;

import java.util.List;
@Component("reAssignTask")
public class ReAssignTaskPolicy implements ServerShutDownPolicy {
    private ZNodePathUtil zNodePathUtil;
    private Balance balance;
    @Autowired
    public void setzNodePathUtil(ZNodePathUtil zNodePathUtil){
        this.zNodePathUtil = zNodePathUtil;
    }
    @Autowired
    public void setBalance(Balance balance){
        this.balance = balance;
    }
    @Override
    public void handleServerShutDown(CuratorFramework client, String nodeName) throws Exception {
        if(balance.getActiveNodeNum() == 0){
            throw new IllegalStateException("No Active Spider Web Server");
        }
        List<String> taskList = client.getChildren().forPath(zNodePathUtil.getTaskPath());
        for(String taskUuid : taskList){
            String owner = new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskOwnerPath(taskUuid)));
            if(owner.equals(nodeName)){
                String balanceOwner = client.getChildren().forPath(zNodePathUtil.getSpiderWebNodePath()).get(balance.next()-1);
                client.transaction().forOperations(client.transactionOp().setData().forPath(zNodePathUtil.getSpecifiedTaskPath(taskUuid), TASK_STATE.WAIT_FOR_RUN.getState().getBytes()),
                        client.transactionOp().setData().forPath(zNodePathUtil.getSpecifiedTaskOwnerPath(taskUuid),balanceOwner.getBytes()));
            }
        }
    }
}
