package spider.nest.management.node.event;

import org.springframework.context.ApplicationEvent;

public class BasicBalanceFixEvent extends ApplicationEvent {
    public BasicBalanceFixEvent(Object source) {
        super(source);
    }
}
