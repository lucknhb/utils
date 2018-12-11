package com.linghong.molian.aspect;

import com.linghong.molian.pojo.Worker;
import com.linghong.molian.utils.DateUtil;
import com.linghong.molian.utils.IPUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 需要修改控制器地址
     */
    @Pointcut("execution(* com.linghong.molian.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void inputRequest(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Worker worker = (Worker) request.getSession().getAttribute("worker");
        if (worker != null) {
            logger.info("{} IP地址:{} 在{}请求 url = {} ", worker.getMobilePhone(), IPUtil.getIpAddress(request),DateUtil.date2SimpleDate(new Date()), request.getRequestURL());
            return;
        }
        logger.info("url = {} 在{} 被匿名访问 IP地址:{}", request.getRequestURL(), DateUtil.date2SimpleDate(new Date()),IPUtil.getIpAddress(request));
    }
}
