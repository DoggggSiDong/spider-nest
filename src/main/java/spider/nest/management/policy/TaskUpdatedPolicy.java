package spider.nest.management.policy;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

public interface TaskUpdatedPolicy extends Policy {
    public void handleTaskUpdatedEvent(PathChildrenCacheEvent event);
}
