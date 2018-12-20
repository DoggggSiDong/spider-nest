package spider.nest.management.node.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.nest.base.ApplicationContextHolder;
import spider.nest.base.PolicyProperties;
import spider.nest.common.TASK_STATE;
import spider.nest.management.balance.Balance;
import spider.nest.management.node.SpiderWebManagement;
import spider.nest.management.policy.ServerShutDownPolicy;
import spider.nest.util.ZNodePathUtil;

import java.util.Iterator;
import java.util.List;

@Component
public class NodeWatcher {
    private CuratorFramework client;
    private ZNodePathUtil zNodePathUtil;
    private final PathChildrenCache childrenCache;
    private Balance balance;
    private PolicyProperties policyProperties;
    private ServerShutDownPolicy serverShutDownPolicy;
    @Autowired
    public NodeWatcher(CuratorFramework client, ZNodePathUtil zNodePathUtil, Balance balance, PolicyProperties policyProperties) throws Exception {
        this.client = client;
        this.zNodePathUtil = zNodePathUtil;
        this.balance = balance;
        this.policyProperties = policyProperties;
        if (null == client.checkExists().forPath(zNodePathUtil.getSpiderWebNodePath())) {
            client.create().forPath(zNodePathUtil.getSpiderWebNodePath(),null);
        }
        childrenCache = new PathChildrenCache(client, zNodePathUtil.getSpiderWebNodePath(), true);
//        Class serverShutDownPolicyClazz = Class.forName(policyProperties.getServerShutDownPolicyClazzName());
//        serverShutDownPolicy = (ServerShutDownPolicy) ApplicationContextHolder.getBean(serverShutDownPolicyClazz);
        serverShutDownPolicy = (ServerShutDownPolicy) ApplicationContextHolder.getBean(policyProperties.getServerShutDownPolicy());
        addNodeWatcher(childrenCache);
    }
    private void addNodeWatcher(final PathChildrenCache childrenCache) throws Exception {
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        childrenCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        switch (event.getType()) {
                            case CHILD_ADDED:
                                balance.setActiveNodeNum(client.getChildren().forPath(zNodePathUtil.getSpiderWebNodePath()).size());
                                break;
                            case CHILD_REMOVED:
                                balance.setActiveNodeNum(client.getChildren().forPath(zNodePathUtil.getSpiderWebNodePath()).size());
                                balance.fixBalance();

                                serverShutDownPolicy.handleServerShutDown(client, balance, getEventNode(event));
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
    }

    private String getEventNode(PathChildrenCacheEvent event) throws Exception {
        String path = event.getData().getPath();
        String node = path.split("/")[3];
        return node;
    }

    public int getActiveNodeNum(){
        return balance.getActiveNodeNum();
    }
}
