package org.smartx.fast.controller;

import com.alibaba.fastjson.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.commons.utils.JsonUtils;
import org.smartx.commons.utils.LoggerUtils;
import org.smartx.commons.utils.SpringContextHolder;
import org.smartx.fast.bean.ApiRequest;
import org.smartx.fast.bean.ApiResponse;
import org.smartx.fast.bean.CombineRequest;
import org.smartx.fast.bean.CombineRequestData;
import org.smartx.fast.bean.CombineResponse;
import org.smartx.fast.bean.CombineResponseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 组合接口
 *
 * @author kext
 */
@RestController
@RequestMapping(value = {"combine", "/v1/combine"})
public class CombineController {

    private static final Logger logger = LoggerFactory.getLogger(CombineController.class);

    @Resource
    private RequestMappingHandlerMapping requestMapping;

    @Value("${fast.api.timeout:10}")
    private int timeout;

    @RequestMapping("")
    public CombineResponse index(HttpServletRequest req, @RequestBody String combineRequestBody) {
        long startTime = System.nanoTime();
        logger.info("Combine request = {}", combineRequestBody);
        CombineResponse combineResponse;
        try {
            CombineRequest combineRequest =
                    JsonUtils.parseJson(combineRequestBody, CombineRequest.class);
            List<CombineRequestData> requestDataList = combineRequest.getData();
            combineResponse = CombineResponse.ok();
            combineResponse.setId(combineRequest.getId());
            List<CombineResponseData> responseDataList = new ArrayList<>();
            // 并行处理
            CompletionService<CombineResponseData> completionService =
                    new ExecutorCompletionService<>(
                            Executors.newCachedThreadPool());
            IntStream.rangeClosed(0, requestDataList.size() - 1).forEach(i -> {
                CombineRequestData data = requestDataList.get(i);
                data.setId(i);
                completionService.submit(() -> doRequestData(req, data));
            });
            for (int i = 0; i < requestDataList.size(); i++) {
                Future<CombineResponseData> f = completionService.poll(timeout, TimeUnit.SECONDS);
                if (f != null) {
                    responseDataList.add(f.get());
                }
            }
            // @SuppressWarnings("unchecked")
            // List<String> responseKeys = (List<String>) JSONPath.eval(responseDataList, "$.key");
            List<String> responseKeys = responseDataList.stream().map(CombineResponseData::getKey)
                    .collect(Collectors.toList());
            // 判断是否存在超时接口
            if (requestDataList.size() != responseDataList.size()) {
                requestDataList.stream().filter(data -> !responseKeys.contains(data.getKey()))
                        .forEach(data -> responseDataList.add(data.getId(),
                                new CombineResponseData(data.getKey(),
                                        ApiResponse.responseTimeout())));
            }
            // 按请求顺序返回
            List<CombineResponseData> sortResponseList =
                    responseDataList.stream().sorted(Comparator.comparingInt(CombineResponseData::getId))
                            .collect(Collectors.toList());

            combineResponse.setData(sortResponseList);
            logger.info("Combine response is {}, total time spent is {}ms",
                    JsonUtils.toJsonString(combineResponse),
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
        } catch (JSONException e) {
            logger.error(LoggerUtils.getErrorMsg("JSON exception, request={}", combineRequestBody),
                    e);
            return CombineResponse.reqContentError();
        } catch (Throwable t) {
            logger.error(
                    LoggerUtils.getErrorMsg("Unexpected error, request={}", combineRequestBody), t);
            return CombineResponse.error();
        }
        return combineResponse;
    }

    private CombineResponseData doRequestData(HttpServletRequest req, CombineRequestData data) {
        String key = data.getKey();
        ApiRequest apiRequest = data.getValue();
        // get url and handler method map
        Map<RequestMappingInfo, HandlerMethod> map = requestMapping.getHandlerMethods();
        ApiController controller = SpringContextHolder.getBean("apiController");
        CombineResponseData combineResponseData = null;
        boolean isUrlMatch = false;
        for (Entry<RequestMappingInfo, HandlerMethod> e : map.entrySet()) {
            RequestMappingInfo info = e.getKey();
            List<String> list = info.getPatternsCondition().getMatchingPatterns(key);
            if (list.size() > 0) {
                isUrlMatch = true;
                // 匹配url
                HandlerMethod hm = e.getValue();
                // get method name
                String[] strs = key.split("/");
                String methodName = strs[strs.length - 1];
                // request data
                String request = JsonUtils.toJsonString(apiRequest);
                combineResponseData = new CombineResponseData();
                combineResponseData.setId(data.getId());
                combineResponseData.setKey(key);
                ApiResponse response;
                try {
                    String className = hm.getBean().toString();
                    // 调用apicontroller的invoke方法
                    response = (ApiResponse) hm.getMethod()
                            .invoke(controller, className, methodName, request, req);
                } catch (Exception e1) {
                    logger.error(LoggerUtils
                            .getErrorMsg("Invoke method error, method={}, request={}",
                                    new Object[]{methodName, request}), e1);
                    response = ApiResponse.error();
                }
                combineResponseData.setValue(response);
                break;
            }
        }
        if (!isUrlMatch) {
            logger.warn("No handler found for url:{}", key);
            combineResponseData = new CombineResponseData();
            combineResponseData.setId(data.getId());
            combineResponseData.setKey(key);
            combineResponseData.setValue(ApiResponse.apiNotExist());
        }
        return combineResponseData;
    }

}
