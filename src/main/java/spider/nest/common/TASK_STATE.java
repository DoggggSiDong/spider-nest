package spider.nest.common;

public enum TASK_STATE {
    INIT("init"),WAIT_FOR_RUN("wait_for_run"),RUNNING("running"),FINISH("finish"),RUNNING_ERROR("running_error")
    ,CLOSE("close"),CLOSE_SUCCESS("close_success"),CLOSE_ERROR("close_error"),
    REJECT("reject"),TIME_OUT("time_out"),SUCCESS("success");
    private String state;
    TASK_STATE(String i) {
        state = i;
    }
    public String getState(){
        return this.state;
    }
}
