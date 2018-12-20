package spider.nest.base;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
    private String operation;
    private boolean success;
    private String msg;
    private Map<String, Object> data;

    @Override
    public String toString() {
        return "JsonResponse{" +
                "operation='" + operation + '\'' +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public JsonResponse(){

    }
    private JsonResponse(String operation, boolean success, String msg, Map<String, Object> data) {
        this.operation = operation;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public JsonResponse put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public String getOperation() {
        return operation;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public static JsonResponse fail(String operation) {
        return new JsonResponse(operation, false, "", new HashMap<>());
    }

    public static JsonResponse fail(String operation, String msg) {
        return new JsonResponse(operation, false, msg, new HashMap<>());
    }

    public static JsonResponse fail(String operation, Map<String, Object> data) {
        return new JsonResponse(operation, false, "", data);
    }

    public static JsonResponse fail(String operation, String msg, Map<String, Object> data) {
        return new JsonResponse(operation, false, msg, data);
    }

    public static JsonResponse success(String operation) {
        return new JsonResponse(operation, true, "", new HashMap<>());
    }

    public static JsonResponse success(String operation, String msg) {
        return new JsonResponse(operation, true, msg, new HashMap<>());
    }

    public static JsonResponse success(String operation, Map<String, Object> data) {
        return new JsonResponse(operation, true, "", data);
    }

    public static JsonResponse success(String operation, String msg, Map<String, Object> data) {
        return new JsonResponse(operation, true, msg, data);
    }

}

