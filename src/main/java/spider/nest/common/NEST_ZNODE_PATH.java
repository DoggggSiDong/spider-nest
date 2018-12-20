package spider.nest.common;

public enum NEST_ZNODE_PATH {
    TASK("/spider-nest/task"),NODE("/spider-nest/node");
    private  String url;
    NEST_ZNODE_PATH(String url) {
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
}
