package spider.nest.base;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import spider.nest.management.policy.ServerShutDownPolicy;
import spider.nest.management.policy.TaskUpdatedPolicy;

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
    @Bean("selected-task-update-policy")
    public TaskUpdatedPolicy getTaskPolicy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(this.taskPolicy);
        return (TaskUpdatedPolicy) clazz.newInstance();
    }
    public void setTaskPolicy(String taskPolicy) {
        this.taskPolicy = taskPolicy;
    }
    @Bean("selected-server-shut-down-policy")
    public ServerShutDownPolicy getServerShutDownPolicy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(this.serverShutDownPolicy);
        return (ServerShutDownPolicy) clazz.newInstance();
    }

    public void setServerShutDownPolicy(String serverShutDownPolicy) {
        this.serverShutDownPolicy = serverShutDownPolicy;
    }

}
