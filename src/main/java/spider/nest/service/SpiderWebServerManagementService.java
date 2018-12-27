package spider.nest.service;

import spider.nest.entity.NodeInfo;

import java.util.List;

public interface SpiderWebServerManagementService {
    public List<NodeInfo> getAll() throws Exception;
    public NodeInfo getSpecified(String nodeName) throws Exception;
    public int getAliveNum() throws Exception;
}
