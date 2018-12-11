package com.linghong.fkdp.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/1 16:07
 * @Version 1.0
 * @Description:
 * QQ  APP ID：101524491    APP Key：ae94f6b791e51a1879ab3635f41608e4
 */
@Entity
@Table(name = "open_user")
public class OpenUser implements Serializable {
    private Long openUserId;
    private String openId;
    private Integer openType;//登录方式  0代表QQ 1代表微信 3代表微博
    private String accessToken;//调用接口需要用到的token
    private String expiredTime;//授权过期时间
    private String nickName;//昵称
    private String avatar;//头像

    @Id
    @GeneratedValue
    public Long getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(Long openUserId) {
        this.openUserId = openUserId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "OpenUser{" +
                "openUserId=" + openUserId +
                ", openid='" + openId + '\'' +
                ", openType=" + openType +
                ", accessToken='" + accessToken + '\'' +
                ", expiredTime='" + expiredTime + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
