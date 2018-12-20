package spider.nest.util;

import org.springframework.stereotype.Component;
import spider.nest.common.NEST_ZNODE_PATH;
import spider.nest.entity.Task;

@Component
public class ZNodePathUtil {
    public String getTaskPath(){
        return NEST_ZNODE_PATH.TASK.getUrl();
    }

    public String getSpecifiedTaskPath(Task task){
        return NEST_ZNODE_PATH.TASK.getUrl() + "/" + task.getUuId();
    }

    public String getSpecifiedTaskPath(String uuid){
        return NEST_ZNODE_PATH.TASK.getUrl() + "/" + uuid;
    }

    public String getSpecifiedTaskOwnerPath(Task task){
        return NEST_ZNODE_PATH.TASK.getUrl() + "/" + task.getUuId() + "/" + "owner";
    }
    public String getSpecifiedTaskOwnerPath(String uuid){
        return NEST_ZNODE_PATH.TASK.getUrl() + "/" + uuid + "/" + "owner";
    }

    public String getSpecifiedTaskInfoPath(Task task){
        return NEST_ZNODE_PATH.TASK.getUrl() + "/" + task.getUuId() + "/" + "info";
    }

    public String getSpecifiedTaskInfoPath(String uuid){
        return NEST_ZNODE_PATH.TASK.getUrl() + "/" + uuid + "/" + "info";
    }

    public String getSpiderWebNodePath(){
        return NEST_ZNODE_PATH.NODE.getUrl();
    }

    public String getSpecifiedSpiderWebNodePath(String nodeName){
        return NEST_ZNODE_PATH.NODE.getUrl() + "/" + nodeName;
    }


}
