package spider.nest.management.node.event;

import org.springframework.context.ApplicationEvent;

public class BasicBalanceUpdatedEvent extends ApplicationEvent {
    private int currentNum;
    public BasicBalanceUpdatedEvent(Object source, int currentNum) {
        super(source);
        this.currentNum = currentNum;
    }
    public void setCurrentNum(int currentNum){
        this.currentNum = currentNum;
    }

    public int getCurrentNum(){
        return currentNum;
    }
}
