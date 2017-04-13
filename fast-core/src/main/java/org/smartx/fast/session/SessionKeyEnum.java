package org.smartx.fast.session;

import java.text.MessageFormat;

/**
 * <b><code>RedisKeyEnum</code></b>
 * <p>
 * 定义所有存储在redis上的Key, expiredTime(seconds).
 * </p>
 * <p>
 * Key前缀格式：类目_分类_子分类
 * </p>
 * <p>
 * Examples: USER_SESSION, 用户session
 * </p>
 * <p>
 * 版本号 version, 默认都填 1.0, 如果这个缓存对象变更后，无法与旧的缓存对象兼容使用: version + 0.1
 * </p>
 * <p>
 * 注释 @ type, 表示此key的存放数据类型: zset , list, set, hash, value
 * </p>
 *
 * @author kext
 * @since fast 1.0
 */
public enum SessionKeyEnum {

    /**
     * session缓存， {0} = user id
     *
     * @type value = sid
     */
    USER_SESSION("USER_SESSION_{0}", 0, 1.0);

    private String key;

    /**
     * hash field
     */
    private String hashKey;

    /**
     * 过期时间，单位：seconds , 0 表示无过期时间。
     */
    private int expiredTime;

    /**
     * 版本号
     */
    private double version;

    /**
     * 集合，数据类型，缓存的最大长度
     */
    private int maxLen;

    SessionKeyEnum(String key, int expiredTime, double version) {
        this.key = key + "_" + version;
        this.expiredTime = expiredTime;
        this.version = version;
    }

    SessionKeyEnum(String key, int expiredTime, double version, int maxLen) {
        this(key, expiredTime, version);
        this.maxLen = maxLen;
    }

    SessionKeyEnum(String key, String hashKey, int expiredTime, double version) {
        this.key = key + "_" + version;
        this.hashKey = hashKey;
        this.expiredTime = expiredTime;
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public static String buildKey(SessionKeyEnum sessionKey, Object... keys) {
        return MessageFormat.format(sessionKey.getKey(), keys);
    }
}
