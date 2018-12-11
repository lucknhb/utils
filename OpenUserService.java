package com.linghong.fkdp.service;

import com.linghong.fkdp.pojo.OpenUser;
import com.linghong.fkdp.pojo.User;
import com.linghong.fkdp.repository.OpenUserRepository;
import com.linghong.fkdp.repository.UserRepository;
import com.nhb.open.login.bean.*;
import com.nhb.open.login.qq.QQService;
import com.nhb.open.login.wechat.WeChatService;
import com.nhb.open.login.weibo.WeiBoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/3 09:36
 * @Version 1.0
 * @Description:
 */
@Service
public class OpenUserService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private QQService qqService = new QQService();
    private WeiBoService weiBoService = new WeiBoService();
    private WeChatService weChatService = new WeChatService();
    //todo 第三方登录相关配置
    //qq配置信息
    @Value("${openUser.qq.appId}")
    private String qqAppId;
    @Value("${openUser.qq.appKey}")
    private String qqAppKey;
    @Value("${openUser.qq.redirectUrl}")
    private String qqRedirectUrl;
    //微信配置信息
    @Value("${openUser.wx.appId}")
    private String wxAppId;
    @Value("${openUser.wx.appKey}")
    private String wxAppKey;
    @Value("${openUser.wx.redirectUrl}")
    private String wxRedirectUrl;
    //微信公众号配置信息
    @Value("${openUser.wx.appIdForClient}")
    private String wxAppIdForClient;
    @Value("${openUser.wx.appKeyForClient}")
    private String wxAppKeyForClient;
    @Value("${openUser.wx.redirectUrlForClient}")
    private String wxRedirectUrlForClient;
    //微博配置信息
    @Value("${openUser.wb.appId}")
    private String wbAppId;
    @Value("${openUser.wb.appKey}")
    private String wbAppKey;
    @Value("${openUser.wb.redirectUrl}")
    private String wbRedirectUrl;

    @Resource
    private OpenUserRepository openUserRepository;
    @Resource
    private UserRepository userRepository;

    public void qqLogin(HttpServletResponse response){
        logger.info("回调地址："+qqRedirectUrl);
        qqService.getCode(qqAppId, qqRedirectUrl, response);
    }

    public boolean qqCallBack(HttpServletRequest request) {
        String code = qqService.callBack(request);
        logger.info("code:"+code);
        QQAccessToken accessToken = qqService.getAccessToken(qqAppId, qqAppKey, code, qqRedirectUrl);
        QQUser qqUser = qqService.getUserMessage(accessToken.getOpenid(), qqAppId, accessToken.getAccess_token());
        OpenUser target = openUserRepository.findByOpenId(accessToken.getOpenid());
        if (target != null){
            target.setAccessToken(accessToken.getAccess_token());
            target.setExpiredTime(accessToken.getExpires_in());
            logger.info("已登录数据：{}",target.toString());
            return true;
        }else {
            User user = new User();
            OpenUser openUser = new OpenUser();
            openUser.setOpenType(0);
            openUser.setOpenId(accessToken.getOpenid());
            openUser.setAccessToken(accessToken.getAccess_token());
            openUser.setNickName(qqUser.getNickname());
            openUser.setAvatar(qqUser.getFigureurl_qq_1());
            openUser.setExpiredTime(accessToken.getExpires_in());
            user.setOpenUser(openUser);
            userRepository.save(user);
            logger.info("新登录数据：{}",openUser.toString());
        }
        return true;
    }

    public void weChatLogin(HttpServletResponse response) {
        weChatService.getCode(wxAppId,wxRedirectUrl , response);
    }

    public void wxLoginForClient(HttpServletResponse response) {
        weChatService.getCodeForClient(wxAppIdForClient, wxRedirectUrlForClient,response );
    }

    public boolean wxCallBack(HttpServletRequest request) {
        String code = weChatService.callBack(request);
        WeChatAccessToken accessToken = weChatService.getAccessToken(wxAppId, wxAppKey, code);
        WeChatUser userMessage = weChatService.getUserMessage(accessToken.getAccess_token(), accessToken.getOpenid());
        OpenUser target = openUserRepository.findByOpenId(accessToken.getOpenid());
        if (target != null){
            target.setAccessToken(accessToken.getAccess_token());
            target.setAvatar(userMessage.getHeadimgurl());
            target.setExpiredTime(accessToken.getExpires_in());
            target.setNickName(userMessage.getNickname());
            target.setOpenId(userMessage.getOpenid());
            logger.info("已登录数据：{}",target.toString());
        }else {
            OpenUser openUser = new OpenUser();
            openUser.setAccessToken(accessToken.getAccess_token());
            openUser.setAvatar(userMessage.getHeadimgurl());
            openUser.setExpiredTime(accessToken.getExpires_in());
            openUser.setNickName(userMessage.getNickname());
            openUser.setOpenId(userMessage.getOpenid());
            openUser.setOpenType(1);
            User user = new User();
            user.setOpenUser(openUser);
            userRepository.save(user);
            logger.info("新登录数据：{}",user.toString());
        }
        return true;
    }

    public boolean wxCallBackForClient(HttpServletRequest request) {
        String code = weChatService.callBack(request);
        logger.info("code:{}",code);
        WeChatAccessToken accessToken = weChatService.getAccessToken(wxAppIdForClient, wxAppKeyForClient, code);
        logger.info("acc:{}",accessToken);
        WeChatUser userMessage = weChatService.getUserMessage(accessToken.getAccess_token(), accessToken.getOpenid());
        OpenUser target = openUserRepository.findByOpenId(accessToken.getOpenid());
        if (target != null){
            target.setAccessToken(accessToken.getAccess_token());
            target.setAvatar(userMessage.getHeadimgurl());
            target.setExpiredTime(accessToken.getExpires_in());
            target.setNickName(userMessage.getNickname());
            target.setOpenId(userMessage.getOpenid());
            logger.info("已登录数据：{}",target.toString());
        }else {
            OpenUser openUser = new OpenUser();
            openUser.setAccessToken(accessToken.getAccess_token());
            openUser.setAvatar(userMessage.getHeadimgurl());
            openUser.setExpiredTime(accessToken.getExpires_in());
            openUser.setNickName(userMessage.getNickname());
            openUser.setOpenId(userMessage.getOpenid());
            openUser.setOpenType(1);
            User user = new User();
            user.setOpenUser(openUser);
            userRepository.save(user);
            logger.info("新登录数据：{}",user.toString());
        }
        return true;
    }

    public void weiBoLogin(HttpServletResponse response) {
        weiBoService.getCode(wbAppId, wbRedirectUrl,response );
    }

    public boolean wbCallBack(HttpServletRequest request) {
        String code = weiBoService.callBack(request);
        WeiBoAccessToken accessToken = weiBoService.getAccessToken(wbAppId, wbAppKey, code, wbRedirectUrl);
        logger.info("accessToken:{}",accessToken.toString());
        WeiBoUser userMessage = weiBoService.getUserMessage(accessToken.getAccess_token(), accessToken.getUid());
        OpenUser target = openUserRepository.findByOpenId(accessToken.getUid());
        if (target != null){
            target.setAccessToken(accessToken.getAccess_token());
            target.setAvatar(userMessage.getProfile_image_url());
            target.setExpiredTime(accessToken.getExpires_in());
            target.setNickName(userMessage.getName());
            target.setOpenId(accessToken.getUid());
            logger.info("已登录数据：{}",target.toString());
        }else {
            OpenUser openUser = new OpenUser();
            openUser.setAccessToken(accessToken.getAccess_token());
            openUser.setAvatar(userMessage.getProfile_image_url());
            openUser.setExpiredTime(accessToken.getExpires_in());
            openUser.setNickName(userMessage.getName());
            openUser.setOpenId(accessToken.getUid());
            openUser.setOpenType(2);
            User user = new User();
            user.setOpenUser(openUser);
            userRepository.save(user);
            logger.info("新登录数据：{}",user.toString());
        }
        return true;
    }

}
