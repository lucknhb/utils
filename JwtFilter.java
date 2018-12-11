package com.linghong.molian.filter;

import com.alibaba.fastjson.JSON;
import com.linghong.molian.bean.Response;
import com.linghong.molian.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 验证jwt签名信息
 * 非大型项目  不要使用此方法   需要前端配合使用
 * 记得添加到webMvcConfig中
 *
 */
public class JwtFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        PrintWriter writer = response.getWriter();
        //指定前端使用jwt 在header中设置的参数为 token
        String auth = request.getHeader("Authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if (auth == null || !auth.startsWith("Bearer ")) {
                writer.append(JSON.toJSONString(new Response(false, 400, null, "身份验证未通过"))).flush();
                writer.close();
                return;
            }
            String token = auth.substring(7);
            try {
                Claims claims = JwtUtil.parseJWT(token);
                request.setAttribute("claims", claims);
                if (!claims.getIssuer().equals("nhb")) {
                    writer.append(JSON.toJSONString(new Response(false, 400, null, "身份验证未通过"))).flush();
                    writer.close();
                    return;
                }
            } catch (SignatureException e) {
                throw new ServletException("Invalid token");
            }
            filterChain.doFilter(servletRequest, servletResponse);
            writer.close();
        }
    }

    @Override
    public void destroy() {

    }
}
