package spider.nest.management.node.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.nest.base.ApplicationContextHolder;
import spider.nest.base.PolicyProperties;
import spider.nest.management.policy.TaskUpdatedPolicy;
import spider.nest.util.ZNodePathUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class TaskWatcher {
    private CuratorFramework client;
    private ZNodePathUtil zNodePathUtil;
    private final PathChildrenCache childrenCache;
    private PolicyProperties policyProperties;
    private TaskUpdatedPolicy taskUpdatedPolicy;
    @Autowired
    public TaskWatcher(CuratorFramework client,ZNodePathUtil zNodePathUtil,PolicyProperties policyProperties) throws Exception {
        this.client = client;
        this.zNodePathUtil = zNodePathUtil;
        this.policyProperties = policyProperties;
//        Class taskPolicyClazz = Class.forName(policyProperties.getTaskPolicyClazzName());
//        this.taskUpdatedPolicy = (TaskUpdatedPolicy) ApplicationContextHolder.getBean(taskPolicyClazz);
        this.taskUpdatedPolicy = (TaskUpdatedPolicy) ApplicationContextHolder.getBean(policyProperties.getTaskPolicy());
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
