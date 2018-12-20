package spider.nest.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("singleton-curator-client-facotory")
public class SingletonCuratorClient {
    private ZookeeperProperties zookeeperProperties;
    @Autowired
    public SingletonCuratorClient(ZookeeperProperties zookeeperProperties) {
        this.zookeeperProperties = zookeeperProperties;
    }

    @Bean("singleton-curator-client")
    @Lazy
    public CuratorFramework getCuratorClient() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperProperties.getAddress(),
                new RetryNTimes(zookeeperProperties.getRetryTimes(), zookeeperProperties.getRetryInterval()));
        client.start();
        init(client);
        return client;
    }

    public void init(CuratorFramework client) {
        try {
            if (client.checkExists().forPath("/spider-nest") == null) {
                client.create().forPath("/spider-nest", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
