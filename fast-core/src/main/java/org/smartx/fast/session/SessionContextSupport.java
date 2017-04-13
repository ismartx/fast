package org.smartx.fast.session;

import org.smartx.fast.exception.InvalidConfigException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
@Component
public class SessionContextSupport {

    private final Map<String, SessionContext> sessionContextMap = new HashMap<>();

    @Value("${fast.api.session:redis}")
    private String key;

    @PostConstruct
    private void init() {
        this.sessionContextMap.put("redis", new RedisSessionContext());
    }

    public SessionContext get() {
        if (!sessionContextMap.containsKey(key)) {
            throw new InvalidConfigException("Invalid fast.api.session, only support none, map, redis");
        }
        return this.sessionContextMap.get(key);
    }
}
