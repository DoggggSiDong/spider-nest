package spider.nest.service.impl;

import org.springframework.stereotype.Service;
import spider.nest.entity.NodeInfo;
import spider.nest.management.node.SpiderWebManagement;
import spider.nest.service.SpiderWebServerManagementService;

import java.util.List;

@Service
public class SpiderWebServerManagementServiceImpl implements SpiderWebServerManagementService {
    private SpiderWebManagement spiderWebManagement;
    public SpiderWebServerManagementServiceImpl(SpiderWebManagement spiderWebManagement){
        this.spiderWebManagement = spiderWebManagement;
    }
    @Override
    public List<NodeInfo> getAll() throws Exception {
        return spiderWebManagement.getAliveNodeInfo();
    }

    @Override
    public NodeInfo getSpecified(String nodeName) throws Exception {
        return spiderWebManagement.getSpecifiedNodeInfo(nodeName);
    }

    @Override
    public int getAliveNum() {
        return spiderWebManagement.getActiveNodeNum();
    }
}
