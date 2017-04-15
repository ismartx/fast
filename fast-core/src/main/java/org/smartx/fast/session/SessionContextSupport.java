package org.smartx.fast.session;

import org.smartx.commons.utils.SpringContextHolder;
import org.smartx.fast.exception.InvalidConfigException;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * <p>
 *
 * </p>
 *
 * @author kext
 * @since 1.0
 */
public class SessionContextSupport {

    private final Map<String, SessionContext> sessionContextMap = new HashMap<>();

    @Value("${fast.api.session:redis}")
    private String key;

    @PostConstruct
    private void init() {
        this.sessionContextMap.put("redis", SpringContextHolder.getBean("redisSessionContext"));
    }

    public SessionContext get() {
        if (!sessionContextMap.containsKey(key)) {
            throw new InvalidConfigException("Invalid fast.api.session value, only support [none, map or redis]");
        }
        return this.sessionContextMap.get(key);
    }
}
