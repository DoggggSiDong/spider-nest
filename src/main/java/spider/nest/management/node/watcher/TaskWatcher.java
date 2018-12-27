package spider.nest.management.node.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import spider.nest.base.PolicyProperties;
import spider.nest.management.policy.TaskUpdatedPolicy;
import spider.nest.util.ZNodePathUtil;

@Component
public class TaskWatcher {
    private CuratorFramework client;
    private ZNodePathUtil zNodePathUtil;
    private final PathChildrenCache childrenCache;
    private TaskUpdatedPolicy taskUpdatedPolicy;
    @Autowired
    public TaskWatcher(CuratorFramework client,ZNodePathUtil zNodePathUtil,
                       @Qualifier("selected-task-update-policy") TaskUpdatedPolicy taskUpdatedPolicy) throws Exception {
        this.client = client;
        this.zNodePathUtil = zNodePathUtil;
        this.taskUpdatedPolicy = taskUpdatedPolicy;
        if (null == client.checkExists().forPath(zNodePathUtil.getTaskPath())) {
            client.create().forPath(zNodePathUtil.getTaskPath(),null);
        }
        childrenCache = new PathChildrenCache(client, zNodePathUtil.getTaskPath(), true);
        addTaskWatcher(childrenCache);
    }

    private void addTaskWatcher(final PathChildrenCache childrenCache) throws Exception {
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        childrenCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        switch (event.getType()) {
                            case CHILD_UPDATED:
                                taskUpdatedPolicy.handleTaskUpdatedEvent(event);
                            default:
                                break;
                        }
                    }
                }
        );
    }


    public int getCurrentTaskNum() throws Exception {
        return client.getChildren().forPath(zNodePathUtil.getTaskPath()).size();
    }


}
