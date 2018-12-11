package com.linghong.dongdong.filter;

import com.alibaba.fastjson.JSON;
import com.linghong.dongdong.dto.Response;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 前后端分离时（仅仅是请求数据，不返回任何页面的情况下）
 */
public class ShiroLoginFilter extends FormAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(ShiroLoginFilter.class);

    /**
     * 当权限没有，被拒绝时进入此方法
     *
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) {
        if (this.isLoginRequest(servletRequest, servletResponse)) {
            return true;
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            Response result = new Response();
            result.set(false, 707, null, "请先登录");
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
