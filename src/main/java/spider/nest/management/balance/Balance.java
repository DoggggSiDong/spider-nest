package spider.nest.management.balance;

import java.util.Iterator;

public interface Balance extends Iterator<Integer> {
    public void setActiveNodeNum(int activeNodeNum);

    public int getActiveNodeNum();
}
