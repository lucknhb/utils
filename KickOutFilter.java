package com.linghong.dongdong.filter;

import com.alibaba.fastjson.JSON;
import com.linghong.dongdong.dto.Response;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 限制登录人数
 */
public class KickOutFilter extends AccessControlFilter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String redirectUrl = null;
    //是否剔除第一个登录的用户
    private boolean isFirstUser = false;
    //默认同一账号同时在线人数
    private int onlineNumber = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public boolean isFirstUser() {
        return isFirstUser;
    }

    public void setFirstUser(boolean firstUser) {
        isFirstUser = firstUser;
    }

    public int getOnlineNumber() {
        return onlineNumber;
    }

    public void setOnlineNumber(int onlineNumber) {
        this.onlineNumber = onlineNumber;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 设置Cache的key的前缀
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("kickOut");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            logger.info("没有记录任何登录信息，直接登录");
            return true;
        }
        Session session = subject.getSession();
        String mobilePhone = (String) subject.getPrincipal();
        //读取session信息
        Serializable sessionId = session.getId();
        Deque<Serializable> deque = cache.get(mobilePhone);
        if (deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(mobilePhone, deque);
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute("kickOut") == null) {
            deque.push(sessionId);
        }
        while (deque.size() > onlineNumber) {
            Serializable kickOutSessionId = null;
            if (isFirstUser) {//判断是否剔除第一个登录的
                kickOutSessionId = deque.removeLast();
            } else {
                kickOutSessionId = deque.removeFirst();
            }
            cache.put(mobilePhone, deque);
            try {
                Session kickOutSession = sessionManager.getSession(new DefaultSessionKey(kickOutSessionId));
                if (kickOutSession != null) {
                    //设置会话的kickOut属性表示踢出了
                    kickOutSession.setAttribute("kickOut", true);
                }
            } catch (Exception e) {//ignore exception
                e.printStackTrace();
            }
        }
        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickOut") != null
                && (Boolean) session.getAttribute("kickOut") == true) {
            //会话被踢出了
            try {
                subject.logout();
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                Response result = new Response();
                result.set(false, 888, null, "账号已被登录，若非本人操作请修改密码");
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json; charset=utf-8");
                httpServletResponse.getWriter().write(JSON.toJSONString(result));
            } catch (Exception e) {
                throw new ConcurrentAccessException("账号已被登录，若非本人操作请修改密码");
            }
//            WebUtils.issueRedirect(request, response, redirectUrl);
            return false;
        }
        return true;
    }
}
