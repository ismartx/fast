package org.smartx.fast.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API请求返回
 *
 * @author kext
 * @since 1.0
 */
public class ApiResponse extends Response {

    private static final long serialVersionUID = 6301246736050579307L;

    private Map<String, Object> data;

    private String etag;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    /**
     * Add object to data
     */
    public ApiResponse addObjectToData(Object obj) throws Exception {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        for (Field f : obj.getClass().getDeclaredFields()) {
            if ("this$0".equals(f.getName())) {
                continue;
            }
            f.setAccessible(true);
            this.data.put(f.getName(), f.get(obj));
        }
        return this;
    }

    public ApiResponse addValueToData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }

    public ApiResponse addSessionUserToData(SessionUser user) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put("user", user);
        return this;
    }

    public ApiResponse addPageToData(Page page) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put("page", page);
        return this;
    }

    public <T> ApiResponse addListToData(List<T> list) {
        return this.addListToData("dataList", list);
    }

    public <T> ApiResponse addListToData(String key, List<T> list) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.data.put(key, list);
        return this;
    }

    /**
     * 不带msg的ok返回码，减少返回信息
     */
    public static ApiResponse ok() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.SUCCESSFUL);
        response.setData(new HashMap<>());
        return response;
    }

    public static ApiResponse okWithDefaultMsg() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.SUCCESSFUL);
        response.setData(new HashMap<>());
        return response;
    }

    public static ApiResponse ok(String msg) {
        ApiResponse response = new ApiResponse();
        response.setState(new State(StateCode.SUCCESSFUL.getCode(), msg));
        response.setData(new HashMap<>());
        return response;
    }

    public static ApiResponse error() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.SERVER_ERROR);
        return response;
    }

    public static ApiResponse error(String msg) {
        ApiResponse response = new ApiResponse();
        response.setState(new State(StateCode.SERVER_ERROR.getCode(), msg));
        return response;
    }

    public static ApiResponse error(int code, String msg) {
        ApiResponse response = new ApiResponse();
        response.setState(new State(code, msg));
        return response;
    }

    public static ApiResponse error(State state) {
        ApiResponse response = new ApiResponse();
        response.setState(state);
        return response;
    }

    public static ApiResponse invalidUid() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.INVALID_UID);
        return response;
    }

    public static ApiResponse invalidSignError() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.INVALID_SIGH_ERROR);
        return response;
    }

    public static ApiResponse missIdError() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.MISS_ID_ERROR);
        return response;
    }

    public static ApiResponse sessionExist() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.SESSION_EXIST);
        return response;
    }

    public static ApiResponse sessionNotExist() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.SESSION_NOT_EXIST);
        return response;
    }

    public static ApiResponse sessionHasLogin() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.SESSION_HAS_LOGIN);
        return response;
    }

    public static ApiResponse missParamError() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.MISS_PARAM_ERROR);
        return response;
    }

    public static ApiResponse mobileNotExist() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.MOBILE_NOT_EXIST);
        return response;
    }

    public static ApiResponse reqContentError() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.REQUEST_CONTENT_ERROR);
        return response;
    }

    public static ApiResponse invalidSessionParam() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.INVALID_SESSION_PARAM);
        return response;
    }

    public static ApiResponse apiNotExist() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.API_NOT_EXIST);
        return response;
    }

    public static ApiResponse responseTimeout() {
        ApiResponse response = new ApiResponse();
        response.setState(StateCode.RESPONSE_TIMEOUT);
        return response;
    }

}
