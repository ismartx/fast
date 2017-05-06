[English](./README.md)

## fast
fast是一个基于Spring MVC的API框架。

### 使用
> java版本要求使用JDK8以上

```
<dependency>
     <groupId>org.smartx</groupId>
     <artifactId>fast-core</artifactId>
     <version>1.0</version>
</dependency>
```

### 配置
#### fast配置
```
fast.api.key - 消息加密salt
fast.api.timeout - 组合请求timeout时间，默认10秒
fast.api.session - session存储，默认是内置的map，其他选择是redis，如果选择redis，请参考下面的配置。
```

#### spring配置
1. json支持
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
支持json，以及支持CombineController的RequestMappingHandlerMapping

2. spring注解配置
```
<context:annotation-config/>
```
让@Resource @Component注解生效。

3. SpringContextHolder bean配置

参考**fast-demo**项目配置。


### session支持
1. 配置bean sessionContextSupport
```
<bean id="sessionContextSupport" class="org.smartx.fast.session.SessionContextSupport" lazy-init="true"/>
```
2. redis支持
- 添加org.smartx.redis in component-scan包扫描
```
<context:component-scan base-package="org.smartx.fast, org.smartx.redis">
    <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```
- 添加jedisCluster bean(参考redis.xml在fast-demo项目)
```
    <bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
        <property name="maxTotal" value="${redis.pool.maxTotal}"/>
        <property name="maxIdle" value="${redis.pool.maxIdle}"/>
        <property name="minIdle" value="${redis.pool.minIdle}"/>
        <property name="testOnBorrow" value="${redis.pool.testOnBorrow}"/>
    </bean>

    <bean id="jedisCluster"
          class="org.smartx.redis.JedisClusterConnectionFactoryBean">
        <property name="servers" value="${redis.servers}"/>
        <property name="poolConfig" ref="jedisPoolConfig"/>
    </bean>
```
- 配置bean redisSessionContext
```
<bean id="redisSessionContext" class="org.smartx.fast.session.RedisSessionContext"/>
```
