package com.linghong.dongdong.config;


import com.linghong.dongdong.filter.ShiroLoginFilter;
import com.linghong.dongdong.shiro.ShiroHashedCredentialsMatcher;
import com.linghong.dongdong.shiro.ShiroRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static javax.servlet.DispatcherType.*;

/**
 * shiro配置类
 * 使用了ehcache当做缓存
 */

@Configuration
public class ShiroConfig {
    private static Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * 配置ehcache管理
     *
     * @return
     */
    @Bean("ehCacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ehCacheManager;
    }

    @Bean("rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
        simpleCookie.setHttpOnly(true);
        //记住我cookie生效时间,默认30天 ,单位秒：60 * 60 * 24 * 30
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * 会话管理器
     * @return sessionManager
     */
//    @Bean("sessionManager")
//    public DefaultWebSessionManager sessionManager(){
//        DefaultWebSessionManager manager = new DefaultWebSessionManager();
//        // 加入缓存管理器
//        manager.setCacheManager(ehCacheManager());
//        // 删除过期的session
//        manager.setDeleteInvalidSessions(true);
//        // 设置全局session超时时间
//        manager.setGlobalSessionTimeout(1 * 60 *1000);
//        // 是否定时检查session
//        manager.setSessionValidationSchedulerEnabled(true);
//        manager.setSessionValidationScheduler(sessionValidationScheduler());
//        manager.setSessionIdUrlRewritingEnabled(false);
//        manager.setSessionIdCookieEnabled(true);
//        return manager;
//    }

    /**
     * session会话验证调度器
     * @return session会话验证调度器
     */
//    @Bean("sessionValidationScheduler")
//    public ExecutorServiceSessionValidationScheduler sessionValidationScheduler() {
//        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
//        //设置session的失效扫描间隔，单位为毫秒
//        sessionValidationScheduler.setInterval(300*1000);
//        return sessionValidationScheduler;
//    }

    /**
     * cookie管理器;
     *
     * @return
     */
    @Bean("rememberMeManager")
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] cipherKey = decoder.decode("wGiHplamyXlVB11UXWol8g==");
        cookieRememberMeManager.setCipherKey(cipherKey);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 自定义realm
     *
     * @param
     * @return
     */
    @Bean("shiroRealm")
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(credentialsMatcher());
        return shiroRealm;
    }

    /**
     *
     * @return
     */
    @Bean("credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        ShiroHashedCredentialsMatcher matcher = new ShiroHashedCredentialsMatcher(ehCacheManager());
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(2);
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }


    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //todo -------------开启注解形式
    @Bean("advisorAutoProxyCreator")
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean("authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

//    //todo 控制同一账号在线人数
//    @Bean("kickOutUserFilter")
//    public KickOutFilter kickOutUserFilter(){
//        KickOutFilter kickOutFilter = new KickOutFilter();
//        kickOutFilter.setCacheManager(ehCacheManager());
//        kickOutFilter.setSessionManager(sessionManager());
//        return kickOutFilter;
//    }

    @Bean("shiroLoginFilter")
    public ShiroLoginFilter shiroLoginFilter(){
        return new ShiroLoginFilter();
    }

    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setRememberMeManager(rememberMeManager());
//        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiro = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = shiro.getFilters();
        filters.put("authc", shiroLoginFilter());
//        filters.put("kickout",kickOutUserFilter());
        shiro.setFilters(filters);
        shiro.setSecurityManager(securityManager);
        //设置拦截路径 为了保证顺序 使用LinkedHashMap
        Map<String, String> urls = new LinkedHashMap<>(16);
        //不拦截任何路径
       // urls.put("/worker/workerLogin", "kickout");
        shiro.setFilterChainDefinitionMap(urls);
        return shiro;
    }

    @Bean("delegatingFilterProxy")
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetBeanName("shiroFilter");
        proxy.setTargetFilterLifecycle(true);
        registration.setFilter(proxy);
        registration.setAsyncSupported(true);
        registration.setDispatcherTypes(ASYNC, REQUEST, FORWARD, INCLUDE, ERROR);
        return registration;
    }
}

