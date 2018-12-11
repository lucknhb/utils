package com.linghong.molian.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 请求第三方数据
 */
public class SomeUtil {
    private static Logger logger = LoggerFactory.getLogger(SomeUtil.class);

    private static OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
        Request request = chain.request().newBuilder().header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .build();
        return chain.proceed(request);
    }).build();

    /**
     * 手机号归属地
     * @param number
     * @return
     */
    public static String getTelePhoneNumberMessage(String number){
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="+number;
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = httpClient.newCall(request).execute();
            String result = response.body().string().split("=")[1];
            return JSON.toJSONString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIPAddress(String ip){
        Request request = new Request.Builder()
                .url("http://ip.taobao.com/service/getIpInfo.php?ip="+ip)
                .get()
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            return JSON.toJSONString(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取快递详情
     * @param expressType
     * @param expressNumber
     * @return
     */
    public static String getExpress(String expressType,String expressNumber) {
        Request request = new Request.Builder().get().url("http://www.kuaidi100.com/query?type=" + expressType + "&postid=" + expressNumber).build();
        try {
            Response response = httpClient.newCall(request).execute();
            return JSON.toJSONString(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] nhb){
        String phoneNumberMessage = getTelePhoneNumberMessage("13592589109");
        logger.info(phoneNumberMessage);
    }
}
