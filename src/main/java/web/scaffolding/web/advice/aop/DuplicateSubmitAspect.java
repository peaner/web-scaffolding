package web.scaffolding.web.advice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import web.scaffolding.core.exception.DuplicateSubmitException;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author: lilongzhou
 * @Description: 防止表单重复提交的拦截器
 * @Date: Created in 18-6-28 上午11:17
 **/
@Aspect
@Component
@Slf4j
public class DuplicateSubmitAspect {

    public static final String  DUPLICATE_TOKEN_KEY="duplicate_token_key";

    @Pointcut("execution(public * dby.zwt.repertory.web.controller..*(..))")
    public void webLog() {

    }

    @Before("webLog() && @annotation(token)")
    public void before(final JoinPoint joinpoint, DuplicateSubmitToken token) {
        if (null != token) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            boolean isSaveSession = token.save();
            if (isSaveSession) {
                String key = getDuplicateTokenKey(joinpoint);
                Object object = request.getSession().getAttribute(key);
                if (null == object) {
                    String uuid = UUID.randomUUID().toString();
                    request.getSession().setAttribute(key.toString(), uuid);
                } else {
                    // 异常输出
                    throw new DuplicateSubmitException("异常");
                }
            }
        }
    }

    /**
     * 获取重复提交的key
     * @param joinpoint
     * @return
     */
    public String getDuplicateTokenKey(JoinPoint joinpoint) {
        String methodName = joinpoint.getSignature().getName();
        StringBuilder key = new StringBuilder(DUPLICATE_TOKEN_KEY);
        key.append(",").append(methodName);
        return key.toString();
    }

    @AfterReturning("webLog() && @annotation(token)")
    public void doAfterReturning (JoinPoint joinPoint, DuplicateSubmitToken token) {
        if (null != token) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            boolean isSaveSession = token.save();
            if (isSaveSession) {
                String key = getDuplicateTokenKey(joinPoint);
                Object object = request.getSession().getAttribute(key);
                if (null != object) {
                    request.getSession(false).removeAttribute(key);
                }
            }
        }
    }

    /**
     * 异常处理
     * @param joinPoint 切面信息
     * @param e 异常信息
     * @param token 重复提交token
     */
    @AfterThrowing(pointcut = "webLog() && @annotation(token)", throwing = "e")
    public void doAfterThrowing (JoinPoint joinPoint, Throwable e, DuplicateSubmitToken token) {
        if (null != token && !(e instanceof DuplicateSubmitException)) {
            // 处理其他异常
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            boolean isSaveSession = token.save();
            if (isSaveSession) {
                String key = getDuplicateTokenKey(joinPoint);
                Object object = request.getSession().getAttribute(key);
                if (null != object) {
                    // 异常情况移除请求重复标记
                    request.getSession(false).removeAttribute(key);
                }
            }
        }
    }

}
