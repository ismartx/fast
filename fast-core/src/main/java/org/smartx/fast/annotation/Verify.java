package org.smartx.fast.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数校验
 *
 * @author binglin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Verify {
    String name() default "";

    boolean isMobile() default false;

    boolean notBlack() default false;

    int maxLength() default Integer.MAX_VALUE;

    int max() default Integer.MAX_VALUE;
}
