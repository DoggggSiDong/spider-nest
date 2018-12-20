package spider.nest.management.node;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.nest.entity.NodeInfo;
import spider.nest.common.TASK_STATE;
import spider.nest.management.exception.NoSuchSpiderWebServerException;
import spider.nest.management.node.watcher.NodeWatcher;
import spider.nest.util.ZNodePathUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Component
public class SpiderWebManagement {
    private CuratorFramework client;
    private ZNodePathUtil zNodePathUtil;
    private NodeWatcher nodeWatcher;

    @Autowired
    public SpiderWebManagement(CuratorFramework client,ZNodePathUtil zNodePathUtil, NodeWatcher nodeWatcher) throws Exception {
        this.client = client;
        this.zNodePathUtil = zNodePathUtil;
        this.nodeWatcher = nodeWatcher;
    }


    public List<NodeInfo> getAliveNodeInfo() throws Exception {
        List<String> nodeList = client.getChildren().forPath(zNodePathUtil.getSpiderWebNodePath());
        List<NodeInfo> result = new LinkedList<>();
        for (String nodeName : nodeList) {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setNodeName(nodeName);
            String ipAndPort = new String(client.getData().forPath(zNodePathUtil.getSpecifiedSpiderWebNodePath(nodeName)));
            nodeInfo.setIp(ipAndPort.split(":")[0]);
            nodeInfo.setPort(ipAndPort.split(":")[1]);
            result.add(nodeInfo);
        }
        return result;
    }

    public NodeInfo getSpecifiedNodeInfo(String nodeName) throws Exception {
        if(client.checkExists().forPath(zNodePathUtil.getSpecifiedSpiderWebNodePath(nodeName)) == null){
            throw new NoSuchSpiderWebServerException("No Spider Web Server Named :" + nodeName);
        }
        NodeInfo nodeInfo = new NodeInfo();
        String ipAndPort = new String(client.getData().forPath(zNodePathUtil.getSpecifiedSpiderWebNodePath(nodeName)));
        nodeInfo.setNodeName(nodeName);
        nodeInfo.setIp(ipAndPort.split(":")[0]);
        nodeInfo.setPort(ipAndPort.split(":")[1]);
        return nodeInfo;
    }

    public int getActiveNodeNum() {
        return nodeWatcher.getActiveNodeNum();
    }





}
