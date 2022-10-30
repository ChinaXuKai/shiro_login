package com.example.shiro_springboot.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限验证的异常处理类
 *
 * @author xukai
 * @create 2022-10-18 10:08
 */
@Slf4j
@ControllerAdvice
public class PermissionException {

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public String unauthorizedException(Exception e) {
        log.info("无权限异常");
        return "无权限";
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String authorizationException(Exception e) {
        log.info("权限认证失败");
        return "权限认证失败";
    }


}
