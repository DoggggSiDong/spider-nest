package spider.nest.management.node.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import spider.nest.base.PolicyProperties;
import spider.nest.management.node.event.BasicBalanceFixEvent;
import spider.nest.management.node.event.BasicBalanceUpdatedEvent;
import spider.nest.management.node.event.publisher.BasicBalanceFixPublisher;
import spider.nest.management.node.event.publisher.BasicBalanceUpdatedPublisher;
import spider.nest.management.policy.ServerShutDownPolicy;
import spider.nest.util.ZNodePathUtil;

import javax.annotation.PostConstruct;

@Component
public class NodeWatcher {
    private CuratorFramework client;
    private ZNodePathUtil zNodePathUtil;
    private final PathChildrenCache childrenCache;
    private ServerShutDownPolicy serverShutDownPolicy;
    private BasicBalanceFixPublisher basicBalanceFixPublisher;
    private BasicBalanceUpdatedPublisher basicBalanceUpdatedPublisher;
    @Autowired
    public NodeWatcher(CuratorFramework client, ZNodePathUtil zNodePathUtil,
                       @Qualifier("selected-server-shut-down-policy") ServerShutDownPolicy serverShutDownPolicy,
                       BasicBalanceFixPublisher basicBalanceFixPublisher,
                       BasicBalanceUpdatedPublisher basicBalanceUpdatedPublisher) throws Exception {
        this.client = client;
        this.zNodePathUtil = zNodePathUtil;
        this.serverShutDownPolicy = serverShutDownPolicy;
        this.basicBalanceFixPublisher = basicBalanceFixPublisher;
        this.basicBalanceUpdatedPublisher = basicBalanceUpdatedPublisher;
        childrenCache = new PathChildrenCache(client, zNodePathUtil.getSpiderWebNodePath(), true);
   }
    @PostConstruct
    public void init() throws Exception {
        if (null == client.checkExists().forPath(zNodePathUtil.getSpiderWebNodePath())) {
            client.create().forPath(zNodePathUtil.getSpiderWebNodePath(),null);
        }
        addNodeWatcher(childrenCache);
    }
    private void addNodeWatcher(final PathChildrenCache childrenCache) throws Exception {
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        childrenCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        int currentNum = 0;
                        switch (event.getType()) {
                            case CHILD_ADDED:
                                currentNum = client.getChildren().forPath(zNodePathUtil.getSpiderWebNodePath()).size();
                                basicBalanceUpdatedPublisher.publish(currentNum);
                                break;
                            case CHILD_REMOVED:
                                currentNum = client.getChildren().forPath(zNodePathUtil.getSpiderWebNodePath()).size();
                                basicBalanceUpdatedPublisher.publish(currentNum);
                                basicBalanceFixPublisher.publish();
                                serverShutDownPolicy.handleServerShutDown(client,getEventNode(event));
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

}
