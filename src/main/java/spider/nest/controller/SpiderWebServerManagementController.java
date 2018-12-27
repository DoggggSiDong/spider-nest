package spider.nest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spider.nest.base.JsonResponse;
import spider.nest.management.exception.NoSuchSpiderWebServerException;
import spider.nest.service.SpiderWebServerManagementService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/spider/nest/server")
public class SpiderWebServerManagementController {
    private SpiderWebServerManagementService spiderWebServerManagementService;
    @Autowired
    public SpiderWebServerManagementController(SpiderWebServerManagementService spiderWebServerManagementService){
        this.spiderWebServerManagementService = spiderWebServerManagementService;
    }
    @RequestMapping(value = "/get/all", method = RequestMethod.POST)
    public Object getAllServerInfo(){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("resultList",spiderWebServerManagementService.getAll());
        } catch (Exception e) {
            return JsonResponse.fail("获取所有活动SpiderWeb服务器信息","内部错误");
        }
        return JsonResponse.success("获取所有活动SpiderWeb服务器信息",result);
    }

    @RequestMapping(value = "/get/specified", method = RequestMethod.POST)
    public Object getSpecifiedServerInfo(String nodeName){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("result",spiderWebServerManagementService.getSpecified(nodeName));
        }
        catch (NoSuchSpiderWebServerException e){
            return JsonResponse.fail("获取指定活动SpiderWeb服务器信息","该节点不存活或不存在");
        }
        catch (Exception e) {
            return JsonResponse.fail("获取指定活动SpiderWeb服务器信息","内部错误");
        }
        return JsonResponse.success("获取指定活动SpiderWeb服务器信息",result);
    }

    @RequestMapping(value = "/get/num", method = RequestMethod.POST)
    public Object getAliveServerNum(){
        Map<String,Object> result = new HashMap<>();
        int num = 0;
        try {
            num = spiderWebServerManagementService.getAliveNum();
        } catch (Exception e) {
            return JsonResponse.fail("获取活动节点数量","内部错误");
        }
        if(num >= 0){
            result.put("aliveNum",num);
            return JsonResponse.success("获取活动节点数量",result);
        }
        else {
            return JsonResponse.fail("获取活动节点数量","内部错误");
        }
    }
}
