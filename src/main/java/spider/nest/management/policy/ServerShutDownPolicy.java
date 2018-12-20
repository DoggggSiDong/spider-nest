package spider.nest.management.policy;

import org.apache.curator.framework.CuratorFramework;
import spider.nest.management.balance.Balance;

public interface ServerShutDownPolicy extends Policy{
    public void handleServerShutDown(CuratorFramework client, Balance balance, String nodeName) throws Exception;
}
