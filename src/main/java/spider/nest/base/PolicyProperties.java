package spider.nest.base;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "policy")
public class PolicyProperties {
    private String taskPolicy;
    private String serverShutDownPolicy;

    @Override
    public String toString() {
        return "PolicyProperties{" +
                "taskPolicy='" + taskPolicy + '\'' +
                ", serverShutDownPolicy='" + serverShutDownPolicy + '\'' +
                '}';
    }

    public String getTaskPolicy() {
        return taskPolicy;
    }

    public void setTaskPolicy(String taskPolicy) {
        this.taskPolicy = taskPolicy;
    }

    public String getServerShutDownPolicy() {
        return serverShutDownPolicy;
    }

    public void setServerShutDownPolicy(String serverShutDownPolicy) {
        this.serverShutDownPolicy = serverShutDownPolicy;
    }

    public String getTaskPolicyClazzName(){
        return "spider.nest.management.policy." + this.taskPolicy;
    }

    public String getServerShutDownPolicyClazzName(){
        return "spider.nest.management.policy" + this.serverShutDownPolicy;
    }

}
