package com.linghong.molian.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 阿里大于短信接口
 * @author luck_nhb
 */
public class SmsUtil {

    /**
     *
     * @param accessKeyId       阿里大于accessKeyId
     * @param accessKeySecret   阿里大于accessKeySecret
     * @param signName          短信签名（阿里大于短信控制台可查）
     * @param templateCode      短信模板（阿里大于短信控制台可查）
     * @param phoneNumber       目标号码
     */
    public static String  sendCode(String accessKeyId,String accessKeySecret,String signName,String templateCode,String phoneNumber) throws Exception {
        // 初始化ascClient需要的几个参数，固定值
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";
        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-
        SendSmsRequest request = new SendSmsRequest();
        //目标手机号
        request.setPhoneNumbers(phoneNumber);
        //短信签名
        request.setSignName(signName);
        //短信模板
        request.setTemplateCode(templateCode);
        //生成六位的数字验证码
        int random = (int) ((Math.random()*9+1)*100000);
        request.setTemplateParam("{\"code\":\"" + random + "\"}");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println(sendSmsResponse.getMessage());
        }else {
            System.out.println(sendSmsResponse.getMessage());
        }
        return String.valueOf(random);
    }
}
