package spider.nest.management.balance;

import org.springframework.stereotype.Component;
@Component
public class BasicBalance implements Balance {
    private volatile static int activeNodeNum = 0;
    private volatile static int currentTaskOwner = 0;
    @Override
    public boolean hasNext(){
        if(activeNodeNum <= 0){
            return false;
        }
        else {
            return true;
        }
    }
    @Override
    public Integer next() {
        if(activeNodeNum == 0){
            throw new IllegalStateException("No Active Spider Web Server");
        }
        currentTaskOwner = currentTaskOwner + 1;
        if (currentTaskOwner > activeNodeNum) {
            currentTaskOwner = 1;
        }
        return currentTaskOwner;
    }

    public void setActiveNodeNum(int activeNodeNum){
        this.activeNodeNum = activeNodeNum;
    }

    public int getActiveNodeNum(){
        return activeNodeNum;
    }

    public void fixBalance(){
        if (activeNodeNum < currentTaskOwner) {
            currentTaskOwner = activeNodeNum;
        }
    }

}
