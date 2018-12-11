package com.linghong.molian.exception;

import com.linghong.molian.bean.Response;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShiroExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(ShiroExceptionHandler.class);

    // 捕捉 AccountException 抛出的异常
    @ExceptionHandler(AccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response accountException(AccountException ex) {
        logger.info("606");
        return new Response(false, 606, null, "认证出现错误,详细信息：" + ex.getMessage());
    }

    @ExceptionHandler({UnauthenticatedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response unauthenticatedException(UnauthenticatedException ex) {
        logger.info("607");
        return new Response(false, 607, null, "认证出现错误,详细信息：请登录后进行操作");
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response authenticationException(AuthenticationException ex) {
        logger.info("611");
        return new Response(false, 611, null, "认证出现错误,详细信息：" + ex.getMessage());
    }

    @ExceptionHandler({ConcurrentAccessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response concurrentAccessException(ConcurrentAccessException ex) {
        return new Response(false, 608, null, "账号已登录,若非本人授权,请修改密码");
    }

    @ExceptionHandler({IncorrectCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response incorrectCredentialsException(IncorrectCredentialsException ex) {
        logger.info("609");
        return new Response(false, 609, null, "认证出现错误,详细信息：" +ex.getMessage());
    }

    @ExceptionHandler({ExcessiveAttemptsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response excessiveAttemptsException(ExcessiveAttemptsException ex) {
        return new Response(false, 610, null, ex.getMessage());
    }
}
