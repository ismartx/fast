## fast
fast is a API framework base on spring mvc.

### config
#### fast config
```
fast.api.key - message sign key
fast.api.timeout - combine request api timeout, default 10s
fast.api.session - 'none' means no need api session, 'map' means use java map stores session datas, 'redis' means use redis stores session datas.
```

#### spring config
1. json support
```
<mvc:annotation-driven>
    <mvc:message-converters register-defaults="true">
        <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
            <property name="supportedMediaTypes">
                <list>
                    <value>text/html;charset=UTF-8</value>
                    <value>application/json;charset=UTF-8</value>
                </list>
            </property>
        </bean>
    </mvc:message-converters>
</mvc:annotation-driven>
```
support RequestMappingHandlerMapping in CombineController and return JSON message format.

2. spring annotations support
```
<context:annotation-config/>
```
support @Resource @Component annotations.

3. SpringContextHolder bean

please reference to **fast-demo** project for more details.


### session support
1. bean sessionContextSupport with lazy-init = true
2. redis support
- add org.smartx.redis in component-scan
- add jedisCluster bean(reference redis.xml in fast-demo project)
- bean redisSessionContext
