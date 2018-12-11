package com.linghong.molian.exception;

import com.linghong.molian.bean.Response;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ValidateExceptionHandler {

    /**
     * 需要验证的参数使用@RequestBody修饰时(通常是POST或PUT的rest请求),
     * 当验证不通过, 会抛出MethodArgumentNotValidException
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer errorMessage = new StringBuffer();
        errorMessage.append("所传参数有误:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField()+"("+fieldError.getDefaultMessage()+") ");
        }
        return new Response(false, 400, null, errorMessage.toString());
    }


    /**
     * 需要验证的参数没有用@RequestBody修饰时(通常时GET或DELETE的rest请求),
     * 当验证不通过, 会抛出BindException
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer errorMessage = new StringBuffer();
        errorMessage.append("所传参数有误:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField()+"("+fieldError.getDefaultMessage()+") ");
        }
        return new Response(false, 400, null, errorMessage.toString());
    }
}