package org.smartx.fast.controller;

import com.alibaba.fastjson.JSONException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.commons.utils.CodeUtils;
import org.smartx.commons.utils.JsonUtils;
import org.smartx.commons.utils.LoggerUtils;
import org.smartx.commons.utils.SpringUtils;
import org.smartx.fast.annotation.ParamCheck;
import org.smartx.fast.annotation.Privilege;
import org.smartx.fast.annotation.Verify;
import org.smartx.fast.bean.ApiRequest;
import org.smartx.fast.bean.ApiResponse;
import org.smartx.fast.bean.Page;
import org.smartx.fast.bean.StateCode;
import org.smartx.fast.session.SessionContext;
import org.smartx.fast.session.SessionContextSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * spring api controller
 *
 * @author kext
 * @since 1.0
 */
@RestController
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Resource
    private SessionContextSupport sessionContextSupport;

    @Value("${fast.api.key:9fbb9f7767b583f3236dbd005c79e301}")
    protected String key;

    @RequestMapping(value = "/{methodName}")
    public ApiResponse invoke(String className, @PathVariable String methodName,
                              @RequestBody String requestBody, HttpServletRequest req) {
        long startTime = System.nanoTime();
        String logClassName = className != null ? className : this.getClass().getName();
        logger.info("class={}, method={}, request={}", logClassName, methodName, requestBody);
        if ("favicon".equals(methodName)) {
            logger.warn("H5 call favicon request, break.");
            return ApiResponse.error(StateCode.FAVICON_INVALID_REQUEST);
        }
        ApiRequest request;
        ApiResponse response;
        String id = null;
        try {
            if (StringUtils.isBlank(requestBody)) {
                logger.error("JSON error, request body is blank, class={}, method={}", logClassName,
                        methodName);
                return ApiResponse.reqContentError();
            }
            // transfer request string to Request object
            request = JsonUtils.parseJson(requestBody, ApiRequest.class);
            request.setIp(getRemoteIP(req)); // Get real ip
            id = request.getId();
            if (StringUtils.isNotBlank(id)) {
                if (this.validSign(request)) {
                    // invoke controller method by methodName
                    if (methodName.contains("-")) {
                        methodName = formatMethodName(methodName);
                    }
                    Method method;
                    Object obj;
                    if (className == null) {
                        // 单一请求
                        obj = this;
                        method = this.getClass().getMethod(methodName, ApiRequest.class);
                    } else {
                        // 组合请求的子接口
                        obj = SpringUtils.getBean(className);
                        method = obj.getClass().getMethod(methodName, ApiRequest.class);
                    }
                    if (method == null) {
                        logger.error("Api not exist error, class={}, method={}, request={}",
                                logClassName, methodName, requestBody);
                        return ApiResponse.apiNotExist();
                    }
                    if (method.isAnnotationPresent(ParamCheck.class)) {
                        ParamCheck paramCheck = method.getDeclaredAnnotation(ParamCheck.class);
                        if (!StringUtils.isAnyBlank(paramCheck.must())) {
                            String[] keys = paramCheck.must();
                            if (request.isMissParam(keys)) {
                                return ApiResponse.missParamError();
                            }
                        }
                        ApiResponse apiResponse;
                        Verify[] verifies = paramCheck.verifies();
                        for (Verify v : verifies) {
                            apiResponse = processVerify(v, request);
                            if (apiResponse != null) {
                                logger.info("class={}, method={}, Response is {}, total time spent is {}ms",
                                        logClassName, methodName, JsonUtils.toJsonString(apiResponse),
                                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
                                return apiResponse;
                            }
                        }
                    }
                    if (method.isAnnotationPresent(Privilege.class)) {
                        Privilege privilege = method.getAnnotation(Privilege.class);
                        if ("login".equals(privilege.type())) {
                            SessionContext sessionContext = this.sessionContextSupport.get();
                            if (!sessionContext.validateSessionParam(request.getSessionUser())) {
                                return ApiResponse.invalidSessionParam();
                            }
                            // validate user session id
                            if (!sessionContext.existSessionId(request)) {
                                // session not exist
                                return ApiResponse.sessionNotExist();
                            }
                            if (!sessionContext.isSameSessionUser(request)) {
                                // session has login
                                return ApiResponse.sessionHasLogin();
                            }
                        }
                    }
                    Page page = request.getPage();
                    if (null != page) {
                        if (page.getPage() < 1) {
                            logger.error("Invalid param,page number={}", page.getPage());
                            return ApiResponse.error(StateCode.INVALID_PARAM);
                        }
                        if (page.getSize() < 0) {
                            logger.error("Invalid param,page size={}", page.getPage());
                            return ApiResponse.error(StateCode.INVALID_PARAM);
                        }
                    }
                    // response result
                    response = (ApiResponse) method.invoke(obj, request);
                    // etag
                    String etag = request.getEtag();
                    // have etag and response ok
                    if (etag != null && response.getState().getCode() == StateCode.SUCCESSFUL.getCode()) {
                        // 计算etag，如果为第一次访问，直接返回
                        String newEtag = calculateEtag(response);
                        if (etag.length() != 0) {
                            // 比较etag
                            if (etag.equals(newEtag)) {
                                // etag相同，返回30001，不用返回数据
                                response.setData(new HashMap<>());
                                response.setState(StateCode.CACHE_NOT_CHANGE);
                            } else {
                                // etag不同，返回30002，返回新数据
                                response.setState(StateCode.CACHE_CHANGED);
                            }
                        }
                        response.setEtag(newEtag);
                    }
                } else {
                    response = ApiResponse.invalidSignError();
                }
            } else {
                response = ApiResponse.missIdError();
            }
        } catch (NoSuchMethodException e) {
            logger.error(
                    LoggerUtils.getErrorMsg("Api not exist error, class={}, method={}, request={}",
                            new Object[]{logClassName, methodName, requestBody}),
                    e);
            response = ApiResponse.apiNotExist();
        } catch (JSONException e) {
            logger.error(LoggerUtils.getErrorMsg("JSON error, class={}, method={}, request={}",
                    new Object[]{logClassName, methodName, requestBody}), e);
            response = ApiResponse.reqContentError();
        } catch (Throwable t) {
            logger.error(
                    LoggerUtils.getErrorMsg("Unexpected error, class={}, method={}, request={}",
                            new Object[]{logClassName, methodName, requestBody}),
                    t);
            response = ApiResponse.error();
        }
        response.setId(id);
        logger.info("class={}, method={}, Response is {}, total time spent is {}ms",
                logClassName, methodName, JsonUtils.toJsonString(response),
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
        return response;
    }

    private ApiResponse processVerify(Verify verify, ApiRequest request) {
        Object param = request.getDataParam(verify.name());
        if (null == param) {
            //这里不处理为null的情况，为null是应该是交给@ParamCheck 的must属性来做
            return null;
        }

        int maxLength = verify.maxLength();
        boolean notBlack = verify.notBlack();
        int max = verify.max();
        //boolean isMobile = verify.isMobile();

        //处理maxLength
        if (maxLength != Integer.MAX_VALUE) {
            if (param instanceof String) {
                if (((String) param).length() > maxLength) {
                    return ApiResponse.error("参数过长");
                }
            }
        }

        //处理notBlack
        if (notBlack) {
            if (param instanceof String) {
                if (StringUtils.isBlank((CharSequence) param)) {
                    return ApiResponse.error("不能为空");
                }
            }
        }

        //处理max
        if (max != Integer.MAX_VALUE) {
            if (param instanceof Integer) {
                if (((Integer) param) > max) {
                    return ApiResponse.error("不在范围");
                }
            }
        }

        //处理mobile
        /*
        if (isMobile) {
            if (param instanceof String) {
                if (StringUtils.isBlank((CharSequence) param) || !MobileUtils.validPureMobile((String) param)) {
                    return ApiResponse.mobileNotExist();
                }
            }
        }
        */
        return null;

    }

    /**
     * etag = md5(response data json)
     */
    private static String calculateEtag(ApiResponse response) {
        String data = JsonUtils.toJsonString(response.getData());
        return CodeUtils.md5(data);
    }

    /**
     * 校验签名
     */
    private boolean validSign(ApiRequest request) {
        String clientSign = request.getSign();
        String serverSign = CodeUtils.md5(request.getId() + key);
        return clientSign.equals(serverSign);
    }

    /**
     * 格式化方法名，method-name -> methodName
     */
    private static String formatMethodName(String methodName) {
        String[] strs = methodName.split("-");
        StringBuilder methodNameBuilder = new StringBuilder(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            methodNameBuilder.append(strs[i].substring(0, 1).toUpperCase())
                    .append(strs[i].substring(1));
        }
        return methodNameBuilder.toString();
    }

    /**
     * 从HttpServletRequest中获取远程IP
     */
    private static String getRemoteIP(HttpServletRequest request) {

        String ip = request.getHeader("X-Real-IP") == null ? null
                : request.getHeader("X-Real-IP").toString();
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for") == null ? null
                    : request.getHeader("x-forwarded-for").toString();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP") == null ? null
                    : request.getHeader("Proxy-Client-IP").toString();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP") == null ? null
                    : request.getHeader("WL-Proxy-Client-IP").toString();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
