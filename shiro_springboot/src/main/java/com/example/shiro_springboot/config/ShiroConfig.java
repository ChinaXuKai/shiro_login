package com.example.shiro_springboot.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.example.shiro_springboot.realm.MyRealm;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author xukai
 * @create 2022-10-15 10:53
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private MyRealm myRealm;


    //配置SecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        //1.创建defaultWebSecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //2.创建加密对象，设置相关属性
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //2.1 设置加密算法
        credentialsMatcher.setHashAlgorithmName("MD5");
        //2.2 设置加盐迭代次数（加盐在myRealm中设置）
        credentialsMatcher.setHashIterations(3);
        //3.将加密对象存储到 myRealm 中
        myRealm.setCredentialsMatcher(credentialsMatcher);
        //4.将 myRealm 存入 defaultWebSecurityManager 对象
        securityManager.setRealm(myRealm);
        //5.设置rememberMe
        securityManager.setRememberMeManager(rememberMeManager());
        //6.设置缓存管理器
        securityManager.setCacheManager(getEhcacheManager());
        //5.返回
        return securityManager;
    }

    //获取缓存管理器
    private EhCacheManager getEhcacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        InputStream is = null;
        try {
            is = ResourceUtils.getInputStreamForPath("classpath:ehcache/ehcache.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CacheManager cacheManager = new CacheManager(is);
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    //创建shiro的cookie管理对象
    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie());
        rememberMeManager.setCipherKey("1234567890987654".getBytes());
        return rememberMeManager;
    }

    //cookie属性设置
    private SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //设置跨域
        //cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setMaxAge(30*24*60*60);
        cookie.setMaxAge(60);
        return cookie;
    }


    /**
     * 多个realm的认证策略设置：如一个realm为校验用户名密码，另一个realm为校验手机号和验证码
     * shiro定义了三种认证策略：AtLeastOneSuccessfulStrategy、FirstSuccessfulStrategy、AllSuccessfulStrategy
     * AtLeastOneSuccessfulStrategy：只要有一个（或更多）的 Realm 验证成功，那么认证将视为成功
     * FirstSuccessfulStrategy：第一个 Realm 验证成功，整体认证将视为成功，且后续 Realm 将被忽略
     * AllSuccessfulStrategy：所有 Realm 验证成功，才视为认证成功
     * ModularRealmAuthenticator 内置的认证策略默认实现是 AtLeastOneSuccessfulStrategy 方式。可以通过配置修改策略
     *
     * @return
     */
//    @Bean
//    public DefaultWebSecurityManager defaultWebSecurityManager(){
//        //1.创建 defaultWebSecurityManager 对象
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        //2.创建认证对象，并设置认证策略
//        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
//        modularRealmAuthenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
//        //3.将认证对象存入 defaultWebSecurityManager 对象中
//        securityManager.setAuthenticator(modularRealmAuthenticator);
//        //4.封装 realm 类为集合
//        List<Realm> list = new ArrayList<>();
//        list.add(myRealm);
//        list.add(myRealm2);
//        //5.将 myRealm 存入 defaultWebSecurityManager 对象
//        securityManager.setRealms(list);
//        //6.返回
//        return securityManager;
//    }


    //配置Shiro内置过滤器拦截范围
    @Bean
    public DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        //设置不认证也可以访问的资源
        definition.addPathDefinition("/user/userLogin", "anon");
        definition.addPathDefinition("/user/userLogin2", "anon");
        definition.addPathDefinition("/user/userRegister", "anon");
        definition.addPathDefinition("/user/login", "anon");
        //设置登出过滤器
        definition.addPathDefinition("/logout", "logout");
        //设置需要进行登录认证的拦截范围
        definition.addPathDefinition("/**", "authc");
        //添加存在用户的过滤器（rememberMe）
        definition.addPathDefinition("/**", "user");
        //返回
        return definition;
    }


    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
