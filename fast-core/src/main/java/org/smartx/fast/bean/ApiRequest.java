package org.smartx.fast.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.commons.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API请求
 *
 * @author kext
 * @since 1.0
 */
public class ApiRequest extends Request {

    private static final long serialVersionUID = 6819137531568804362L;

    private final Logger logger = LoggerFactory.getLogger(ApiRequest.class);

    private Client client = new Client();

    private Map<String, Object> data;

    private String encrypt = "md5";

    private String etag;

    private String ip;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDataParamAsString(String name) {
        return getDataParam(name) != null ? getDataParam(name).toString() : null;
    }

    public String getDataParamAsNoEmptyString(String name) {
        return getDataParam(name) != null && (!StringUtils.isBlank(getDataParam(name).toString())) ? getDataParam(name).toString() : null;
    }

    public Integer getDataParamAsInt(String name) {
        return !StringUtils.isBlank(getDataParamAsString(name)) ?
                Integer.valueOf(getDataParamAsString(name)) :
                null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getDataParamAsMap(String name) {
        Object o = this.getDataParam(name);
        return (o instanceof Map) ? (Map<String, Object>) o : null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getDataParamAsType(String name, Class<T> clazz) {
        Object o = this.getDataParam(name);
        if (clazz.isInstance(o)) {
            return (T) o;
        } else if (o instanceof JSONObject) {
            try {
                return JsonUtils.parseJson(((JSONObject) o).toJSONString(), clazz);
            } catch (Exception e) {
                logger.error("Get data param as type error", e);
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getDataParamAsList(String name, Class<T> clazz) {
        Object o = this.getDataParam(name);
        List<T> list = new ArrayList<T>();
        if (clazz.isInstance(o)) {
            list.add((T) o);
            return list;
        } else if (o instanceof JSONArray) {
            try {
                JSONArray arrs = (JSONArray) o;
                arrs.forEach(obj -> {
                    list.add(JsonUtils.parseJson(JsonUtils.toJsonString(obj), clazz));
                });
                return list;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public Object getDataParam(String name) {
        if (data == null || name == null) {
            return null;
        }
        return data.get(name);
    }

    public List<String> getParamStringList(String param) {
        return Arrays.asList(this.getDataParamAsString(param).split("\\,"));
    }

    public List<Integer> getParamIntList(String param) {
        return Arrays.stream(this.getDataParamAsString(param).split("\\,"))
                .map(Integer::valueOf).collect(Collectors.toList());
    }

    public SessionUser getSessionUser() {
        if (this.getData() == null || this.getData().get("user") == null) {
            return null;
        }
        JSONObject jsonObject = (JSONObject) this.getData().get("user");
        return JSONObject.toJavaObject(jsonObject, SessionUser.class);
    }

    public Page getPage() {
        Map<String, Object> map = this.getData();
        if (null == map) {
            return null;
        }
        JSONObject jsonObject = (JSONObject) map.get("page");
        if (null == jsonObject) {
            return null;
        }
        return JSONObject.toJavaObject(jsonObject, Page.class);
    }

    public boolean isMissParam(String... keys) {
        return !Arrays.asList(keys).parallelStream().allMatch(key -> this.data.containsKey(key));
    }

    public boolean isMissParamInSubMap(String mapKey, String... keys) {
        return !Arrays.asList(keys).parallelStream().allMatch(
                key -> this.getDataParamAsMap(mapKey) != null && this.getDataParamAsMap(mapKey)
                        .containsKey(key));
    }

    public Long getDataParamAsLong(String name) {
        return getDataParamAsString(name) != null ? Long.valueOf(getDataParamAsString(name)) : null;
    }

}
