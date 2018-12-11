package com.linghong.fkdp.controller;

import com.linghong.fkdp.dto.Response;
import com.linghong.fkdp.service.OpenUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/3 09:36
 * @Version 1.0
 * @Description:
 */
@RestController
public class OpenUserController {
    @Resource
    private OpenUserService openUserService;

    /**
     * qq登录请求路径
     * @param response
     */
    @GetMapping("/open/qqLogin")
    public void qqLogin(HttpServletResponse response){
        openUserService.qqLogin(response);
    }

    /**
     * QQ登录回调路径
     * @param request
     * @return
     */
    @RequestMapping("/open/qqCallBack")
    public Response qqCallBack(HttpServletRequest request){
        boolean flag = openUserService.qqCallBack(request);
        if (flag){
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"登录失败" );
    }

    /**
     * 微信登录
     * @param response
     */
    @GetMapping("/open/wxLogin")
    public void wxLogin(HttpServletResponse response){
        openUserService.weChatLogin(response);
    }

    /**
     * 微信用于公众号获取用户信息
     * @param response
     */
    @RequestMapping("/open/wxLoginForClient")
    public void wxLoginForClient(HttpServletResponse response){
        openUserService.wxLoginForClient(response);
    }

    /**
     * 微信登录回调地址
     * @param request
     * @return
     */
    @RequestMapping("/open/wxCallBack")
    public Response wxCallBack(HttpServletRequest request){
        boolean flag = openUserService.wxCallBack(request);
        if (flag){
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"登录失败" );
    }

    /**
     * 微信公众号登录回调地址
     * @param request
     * @return
     */
    @RequestMapping("/open/wxCallBackForClient")
    public Response wxCallBackForClient(HttpServletRequest request){
        boolean flag = openUserService.wxCallBackForClient(request);
        if (flag){
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"登录失败" );
    }

    /**
     * 微博登录
     * @param response
     */
    @RequestMapping("/open/wbLogin")
    public void weiBoLogin(HttpServletResponse response){
        openUserService.weiBoLogin(response);
    }

    /**
     * 微博登录回调地址
     * @param request
     * @return
     */
    @RequestMapping("/open/wb")
    public Response wbCallBack(HttpServletRequest request){
        boolean flag = openUserService.wbCallBack(request);
        if (flag){
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"登录失败" );
    }


}
