package spider.nest.management.balance;

import java.util.Iterator;

public interface Balance extends Iterator<Integer> {
    default boolean hasNext() {
        return true;
    }
    public void setActiveNodeNum(int activeNodeNum);
    public int getActiveNodeNum();
    public void fixBalance();
}
