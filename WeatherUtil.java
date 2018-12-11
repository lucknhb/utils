package com.linghong.molian.utils;

import com.alibaba.fastjson.JSON;
import com.linghong.molian.bean.DayWeather;
import com.linghong.molian.bean.RealWeather;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 天气查询接口
 */
public class WeatherUtil {
    private static Logger logger = LoggerFactory.getLogger(WeatherUtil.class);
    private static Map<String, String> cities = new HashMap<>();

    public WeatherUtil() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/weatherCity.properties");
            properties.load(inputStream);
            Enumeration propertyNames = properties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String key = (String) propertyNames.nextElement();
                String value = properties.getProperty(key);
                cities.put(key, value);
                logger.info(key+"---"+value);
            }
        } catch (IOException e) {
            logger.error("未找到weatherCity.properties文件");
            e.printStackTrace();
        }
    }

    /**
     * 获取实时天气
     *
     * @param city
     * @return
     */
    public static RealWeather getRealWeather(String city) {
        if (cities == null){
            new WeatherUtil();
        }
        String url = "http://www.weather.com.cn/data/sk/" + cities.get(city) + ".html";
        logger.info("url:"+url);
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).get().build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("获取天气失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RealWeather weather = JSON.parseObject(response.body().string(), RealWeather.class);
                logger.info(JSON.toJSONString(weather));
            }
        });
        return null;
    }

    /**
     * 获取一天天气情况
     *
     * @param city
     * @return
     */
    public static DayWeather getDayWeather(String city) {
        if (cities == null){
            new WeatherUtil();
        }
        String url = "http://www.weather.com.cn/data/cityinfo/" + cities.get(city) + ".html";
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).get().build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("获取天气失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DayWeather weather = JSON.parseObject(response.body().string(), DayWeather.class);
                logger.info(JSON.toJSONString(weather));
            }
        });
        return null;
    }
}
