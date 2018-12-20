package spider.nest.management.policy;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.stereotype.Component;

@Component("noAction")
public class NoActionPolicy implements TaskUpdatedPolicy {
    @Override
    public void handleTaskUpdatedEvent(PathChildrenCacheEvent event) {

    }
}
