package com.example.shiro_springboot.controller;

import com.example.shiro_springboot.lang.Result;
import com.example.shiro_springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author xukai
 * @create 2022-10-15 11:24
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //跳转登录页面
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //登录认证验证rememberMe
    @GetMapping("/userLoginRm")
    public String userLoginRm(HttpSession session) {
        session.setAttribute("user", "rememberMe");
        return "main";
    }

    /**
     * 用户登录
     *
     * @param name
     * @param pwd
     * @param rememberMe 传ture时意为开启rememberMe，传false时意为不开启rememberMe，
     * @return 登录成功跳转到main页面，失败跳转到error页面
     */
    @GetMapping("/userLogin")
    public String userLogin(String name, String pwd,
                            @RequestParam(defaultValue = "false") boolean rememberMe,
                            HttpSession session) {
        //1.获取subject对象
        Subject subject = SecurityUtils.getSubject();
        //2.封装请求数据到token
        AuthenticationToken token = new UsernamePasswordToken(name, pwd, rememberMe);
        //3.调用Subject 的 login 方法进行登录认证
        try {
            subject.login(token);
            log.info("用户的身份信息={}", token.getPrincipal().toString());
            session.setAttribute("user", token.getPrincipal().toString());
            return "main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/userLogin2")
    public Result userLogin2(String name, String pwd) {
        //1.获取subject对象
        Subject subject = SecurityUtils.getSubject();
        //2.封装请求数据到token
        AuthenticationToken token = new UsernamePasswordToken(name, pwd);
        //3.调用Subject 的 login 方法进行登录认证，认证失败则有异常
        try {
            subject.login(token);
            return Result.succ(200, "登录成功", name);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return Result.fail("查询不到该用户，请检查用户名密码");
        }
    }


    @GetMapping("/userRegister")
    public Result userRegister(String name, String pwd) {
        return Result.succ(200, "注册成功", name);
    }

    /**
     * 登录认证验证角色
     *
     * @return
     * @RequiresRole 验证subject是否有相应角色，有角色访问方法，没有则会抛出异常 AuthorizationException。
     */
    @RequiresRoles("admin")
    @GetMapping("/userLoginRoles")
    @ResponseBody
    public String userLoginRoles() {
        log.info("subject有admin角色");
        return "验证角色成功";
    }

    /**
     * 登录认证验证权限
     *
     * @return
     * @RequiresPermissions 验证subject是否有相应权限，有权限访问方法，没有则会抛出异常 AuthorizationException。
     */
    @RequiresPermissions("user:add")
    @GetMapping("/userPermission")
    @ResponseBody
    public String userPermission() {
        log.info("登录认证验证权限成功");
        return "验证权限成功";
    }
}
