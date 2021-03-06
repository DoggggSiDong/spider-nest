package spider.nest.management.policy;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.nest.common.TASK_STATE;
import spider.nest.management.balance.Balance;
import spider.nest.util.ZNodePathUtil;

import java.util.List;
@Component("cleanTask")
public class CleanTaskPolicy implements ServerShutDownPolicy {
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
        List<String> taskList = client.getChildren().forPath(zNodePathUtil.getTaskPath());
        for(String taskUuid : taskList){
            String owner = new String(client.getData().forPath(zNodePathUtil.getSpecifiedTaskOwnerPath(taskUuid)));
            if(owner.equals(nodeName)){
                client.delete().deletingChildrenIfNeeded().forPath(zNodePathUtil.getSpecifiedTaskPath(taskUuid));
            }
        }
        if(balance.getActiveNodeNum() == 0){
            throw new IllegalStateException("No Active Spider Web Server");
        }
    }

}
