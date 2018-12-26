package spider.nest.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperProperties {
    @Value("${zk.addr}")
    private String address;
    private Integer retryTimes;
    private Integer retryInterval;
    @Override
    public String toString() {
        return "ZookeeperProperties{" +
                "address='" + address + '\'' +
                ", retryTimes=" + retryTimes +
                ", retryInterval=" + retryInterval +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }
}
