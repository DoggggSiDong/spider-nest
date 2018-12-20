package spider.nest.entity;

public class NodeInfo {
    private String nodeName;
    private String ip;
    private String port;

    @Override
    public String toString() {
        return "NodeInfo{" +
                "nodeName='" + nodeName + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String hostName) {
        this.nodeName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
