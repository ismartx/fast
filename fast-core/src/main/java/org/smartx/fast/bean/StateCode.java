package org.smartx.fast.bean;


/**
 * 返回码
 *
 * @author kext
 * @since 1.0
 */
public final class StateCode {
    // common code
    public static final State SUCCESSFUL = new State(0, "成功");
    public static final State SERVER_ERROR = new State(10001, "服务异常");
    public static final State MISS_PARAM_ERROR = new State(10002, "缺失参数异常");
    public static final State INVALID_SIGH_ERROR = new State(10003, "无效签名");
    public static final State MISS_ID_ERROR = new State(10004, "请求参数缺少id");
    public static final State RESPONSE_TIMEOUT = new State(10005, "接口超时");
    public static final State API_NOT_EXIST = new State(10006, "接口不存在");
    public static final State FAVICON_INVALID_REQUEST = new State(10007, "favicon非法请求");
    public static final State REQUEST_FREQUENCY_LIMIT = new State(10008, "请求频率限制");
    public static final State PROCESSING = new State(10009, "系统正在处理，请耐心等待");
    public static final State NETWORK_ERROR = new State(10010, "网络发生波动，稍等一下再发起请求");
    public static final State REQUEST_CONTENT_ERROR = new State(10011, "请求内容错误");
    public static final State INVALID_UID = new State(10012, "非法的uid");
    public static final State SESSION_EXIST = new State(10013, "SESSION已存在");
    public static final State SESSION_NOT_EXIST = new State(10014, "SESSION不存在");
    public static final State SESSION_HAS_LOGIN = new State(10015, "您的账号在在别处登录，请重新登陆");
    public static final State INVALID_SESSION_PARAM = new State(10016, "SESSION参数缺失");
    public static final State INVALID_PARAM = new State(10017, "无效参数");
    public static final State MOBILE_NOT_EXIST = new State(10018, "手机号码不存在");

    // etag
    public static final State CACHE_NOT_CHANGE = new State(30001, "缓存数据未变化");
    public static final State CACHE_CHANGED = new State(30002, "缓存数据发生变化");
}
