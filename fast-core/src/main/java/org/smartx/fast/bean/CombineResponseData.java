package org.smartx.fast.bean;

/**
 * API组合返回数据
 *
 * @author kext
 */
public class CombineResponseData {

    /**
     * 用于排序
     */
    private transient int id;

    private String key;

    private ApiResponse value;

    public CombineResponseData() {
    }

    public CombineResponseData(String key, ApiResponse value) {
        this.key = key;
        this.value = value;
    }

    public CombineResponseData(int id, String key, ApiResponse value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

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

    public ApiResponse getValue() {
        return value;
    }

    public void setValue(ApiResponse value) {
        this.value = value;
    }
}
