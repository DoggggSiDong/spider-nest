package spider.nest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spder.task.Task;
import spider.nest.base.JsonResponse;
import spider.nest.management.exception.IllegalTaskStateException;
import spider.nest.management.exception.NoActicveSpiderWebServerExcpetion;
import spider.nest.management.exception.TaskExistException;
import spider.nest.service.RunningTaskManagementService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/spider/nest/task/")
public class RunningTaskManagementController {
    private RunningTaskManagementService runningTaskManagementService;
    @Autowired
    public RunningTaskManagementController(RunningTaskManagementService runningTaskManagementService){
        this.runningTaskManagementService = runningTaskManagementService;
    }
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Object submitTask(Task task){
        try {
            runningTaskManagementService.submitTask(task);
        }catch (NoActicveSpiderWebServerExcpetion e){
            return JsonResponse.fail("提交任务","无可用任务节点");
        }
        catch (TaskExistException e){
            return JsonResponse.fail("提交任务","任务存在");
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.fail("提交任务","内部错误");
        }
        return JsonResponse.success("提交任务","提交成功");
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Object search(){
        runningTaskManagementService.search();
        return null;
    }

    @RequestMapping(value = "/get/specified/running", method = RequestMethod.POST)
    public Object get(Task task){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("result",runningTaskManagementService.get(task));
        } catch (Exception e) {
            return JsonResponse.fail("获取指定执行中任务信息","内部错误");
        }
        return JsonResponse.success("获取指定执行中任务信息",result);
    }

    @RequestMapping(value = "/get/all/running",method = RequestMethod.POST)
    public Object getAll(){
        Map<String,Object> result = new HashMap<>();
        try {
           result.put("resultList",runningTaskManagementService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.fail("获取所有执行中任务信息","内部错误");
        }
        return JsonResponse.success("获取指定执行中任务信息",result);
    }

    @RequestMapping(value = "/get/taskNum/running", method = RequestMethod.POST)
    public Object getTaskNum(){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("result",runningTaskManagementService.getTaskNum());
        } catch (Exception e) {
            return JsonResponse.fail("获取执行中任务数量","内部错误");
        }
        return JsonResponse.success("获取执行中任务数量",result);
    }

    @RequestMapping(value = "/close/running", method = RequestMethod.POST)
    public Object close(Task task) {
        try {
            runningTaskManagementService.close(task);
        } catch (Exception e) {
            return JsonResponse.fail("删除指定执行中任务","内部错误");
        }
        return JsonResponse.success("删除执行中任务","删除任务已提交");
    }

//    @RequestMapping(value = "/delete/running",method = RequestMethod.POST)
//    public Object delete(Task task){
//        try {
//            runningTaskManagementService.delete(task);
//        }
//        catch (IllegalTaskStateException e){
//            return JsonResponse.fail("删除执行中任务","任务状态不正确,无法删除");
//        } catch (Exception e) {
//            return JsonResponse.fail("删除执行中任务","内部错误");
//        }
//        return JsonResponse.success("删除执行中任务","成功删除");
//    }

    @RequestMapping(value = "/restart",method = RequestMethod.POST)
    public Object restart(Task task){
        try {
            runningTaskManagementService.restart(task);
        }
        catch (IllegalTaskStateException e){
            return JsonResponse.fail("重启任务","任务状态不正确,无法重启");
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.fail("重启任务","内部错误");
        }
        return JsonResponse.success("重启任务","重启任务成功");
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object update(){
        runningTaskManagementService.update();
        return null;
    }

}
