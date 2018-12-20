package spider.nest.service;

import spder.task.Task;

import java.util.List;

public interface RunningTaskManagementService {
    public void submitTask(Task task) throws Exception;
    public Object search();
    public Task get(Task task) throws Exception;
    public void close(Task task) throws Exception;
    public void update();
    public List<Task> getAll() throws Exception;
    public int getTaskNum() throws Exception;

//    public void delete(Task task) throws Exception;

    public void restart(Task task) throws Exception;
}
