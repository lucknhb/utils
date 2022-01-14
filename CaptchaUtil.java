package com.nhb.consumer.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author: luck_nhb
 * @createDate: 2022-01-14 14:42:55
 * @version: 1.0.0
 * @description: 验证码
 *
 *   <dependency>
 *             <groupId>com.github.penggle</groupId>
 *             <artifactId>kaptcha</artifactId>
 *             <version>2.3.2</version>
 *         </dependency>
 */
public class CaptchaUtil {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaUtil.class);

    private static DefaultKaptcha defaultKaptcha;

    private static final String CAPTCHA_KEY = "captcha";

    //初始化
    static {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "yes");
        properties.put("kaptcha.border.color","105,179,90");
        properties.put("kaptcha.textproducer.font.color","black");
        properties.put("kaptcha.image.width","100");
        properties.put("kaptcha.image.height","50");
        properties.put("kaptcha.textproducer.font.size","27");
        properties.put("kaptcha.session.key","code");
        properties.put("kaptcha.textproducer.char.length","4");
        properties.put("kaptcha.textproducer.font.names","宋体,楷体,微软雅黑");
        properties.put("kaptcha.textproducer.char.string","0123456789ABCEFGHIJKLMNOPQRSTUVWXYZ");
        properties.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
        // properties.put("kaptcha.noise.color","black");
        properties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        properties.put("kaptcha.background.clear.from", Color.gray);
        properties.put("kaptcha.background.clear.to","white");
        properties.put("kaptcha.textproducer.char.space","3");
        defaultKaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
    }

    /**
     * 生成验证码
     *
     * @return
     */
    public static Map<String, Object> generateCaptchaCode(){
        Map<String, Object> map = new HashMap<>();
        // 生成文字验证码
        String text = defaultKaptcha.createText();
        logger.debug("验证码生成文字:{}", text);
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = defaultKaptcha.createImage(text);
        outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        map.put("img", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        //生成验证码对应的token  以token为key  验证码为value存在redis中
        String codeToken = UUID.randomUUID().toString();
        // 验证码有效期60s
        //cacheService.set(CAPTCHA_KEY + codeToken, text, 60);
        map.put("captchaToken", codeToken);
        return map;
    }

    /**
     * 从 redis 中读取 captchaCode 对应的 key，读取完后需要删除 redis 中的缓存
     *
     * @param captchaCode 验证码
     */
    public static boolean checkCaptchaCode(String captchaCode){

        return true;
    }


}
