package com.linghong.fkdp.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT token 仅仅用于校验请求
 * 在http请求头中 Authorization 带上token进行操作
 */
public class JwtUtil {
    /**
     * 加密密文
     */
    public static final String JWT_SECRET = "nhbLoveWyb";
    public static final int JWT_TTL = 12 * 60 * 60 * 1000;  //过期时间

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static Key generalKey() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWT_SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    /**
     * 创建jwt
     * @param claims 键值对参数
     * @return
     * @throws Exception
     */
    public static String createJWT(Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm sha256 = SignatureAlgorithm.HS256;
        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了
        Key key = generalKey();

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder(); // 这里其实就是new一个JwtBuilder，设置jwt的body
        if (claims != null){
            builder.setClaims(claims);       // 私有声明，类似于带参数 键值对
        }
        builder.setId(IDUtil.getId())      // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(new Date(nowMillis))//jwt的签发时间
                .setIssuer("nhb")          //jwt签发人
                .setSubject("allUser")        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(sha256, key);     // 设置签名使用的签名算法和签名使用的秘钥
        // 设置过期时间
        if (JWT_TTL >= 0) {
            long expMillis = nowMillis + JWT_TTL;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) {
        Key key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt)
                .getBody();     //设置需要解析的jwt
        return claims;
    }
}
