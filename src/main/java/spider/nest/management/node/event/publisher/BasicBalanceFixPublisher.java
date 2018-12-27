package spider.nest.management.node.event.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import spider.nest.management.node.event.BasicBalanceFixEvent;

@Component
public class BasicBalanceFixPublisher {
    private ApplicationContext applicationContext;
    @Autowired
    public BasicBalanceFixPublisher(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public void publish(){
        this.applicationContext.publishEvent(new BasicBalanceFixEvent(this));
    }
}
