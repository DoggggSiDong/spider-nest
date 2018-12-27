package spider.nest.management.node.event.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import spider.nest.management.node.event.BasicBalanceUpdatedEvent;


@Component
public class BasicBalanceUpdatedPublisher {
    private ApplicationContext applicationContext;
    @Autowired
    public BasicBalanceUpdatedPublisher(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    public void publish(int currentNum){
        applicationContext.publishEvent(new BasicBalanceUpdatedEvent(this,currentNum));
    }
}
