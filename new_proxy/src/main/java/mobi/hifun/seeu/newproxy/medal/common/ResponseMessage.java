package mobi.hifun.seeu.newproxy.medal.common;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessage {

    /**
     * 请求成功
     * @return : 加上成功状态返回
     */
    public static Map success() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", ResponseCode.OK.getCode());
        result.put("msg", ResponseCode.OK.getMessage());
        return result;
    }

    /**
     * 请求成功
     * @param data : 要返回的数据
     * @return : 加上成功状态返回
     */
    public static Map success(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", ResponseCode.OK.getCode());
        result.put("msg", ResponseCode.OK.getMessage());
        result.put("data", data);
        return result;
    }

    /**
     * 请求失败
     * @return : 加上成功状态返回
     */
    public static Map fail() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", ResponseCode.FAIL.getCode());
        result.put("msg", ResponseCode.FAIL.getMessage());
        return result;
    }

    /**
     * 请求失败
     * @return : 加上成功状态返回
     */
    public static Map error(int code,String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", code);
        result.put("msg", message);
        return result;
    }


    /**
     * 请求失败
     * @return : 加上成功状态返回
     */
    public static Map error(ResponseCode responseCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", responseCode.getCode());
        result.put("msg", responseCode.getMessage());
        return result;
    }

    /**
     * 请求参数为空
     * @return : 加上成功状态返回
     */
    public static Map errorParamEmply(String paramName) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", ResponseCode.PARAM_EMPLY.getCode());
        result.put("msg", ResponseCode.PARAM_EMPLY.getMessage() + "：" + paramName);
        return result;
    }


    /**
     * 请求参数无效
     * @param paramName
     * @return
     */
    public static Map errorParamInvalid(String paramName) {
        Map<String, Object> result = new HashMap();
        result.put("state", ResponseCode.PARAM_INVALID.getCode());
        result.put("msg", ResponseCode.PARAM_INVALID.getMessage() + "：" + paramName);
        return result;
    }



}
