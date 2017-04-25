package org.smartx.fast.bean;

/**
 * API组合请求数据
 *
 * @author kext
 * @since 1.0
 */
public class CombineRequestData {

    /**
     * 用于排序
     */
    private transient int id;

    private String key;

    private ApiRequest value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ApiRequest getValue() {
        return value;
    }

    public void setValue(ApiRequest value) {
        this.value = value;
    }
}
