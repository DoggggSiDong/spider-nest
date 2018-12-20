package spider.nest.entity;

public class Task {
    private String uuId;
    private String company;
    private String templateName;
    private String state;
    private String owner;
    private Integer period = 0;

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "Task{" +
                "uuId='" + uuId + '\'' +
                ", company='" + company + '\'' +
                ", templateName='" + templateName + '\'' +
                ", state='" + state + '\'' +
                ", owner='" + owner + '\'' +
                ", period=" + period +
                '}';
    }
}
