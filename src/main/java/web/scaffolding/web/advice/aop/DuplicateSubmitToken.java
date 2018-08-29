package web.scaffolding.web.advice.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lilongzhou
 * @Description: 防止表单重复提交的注解
 * @Date: Created in 18-6-28 上午11:15
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DuplicateSubmitToken {

    boolean save() default false;

    boolean remove() default false;
}
