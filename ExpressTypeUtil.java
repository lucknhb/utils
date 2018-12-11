package com.shudong.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 快递公司列表
 */
public class ExpressTypeUtil {

    /**
     * 获取快递公司列表
     * @return
     */
    public static Map<String,String> getExpressType(){
        Map<String,String> type = new HashMap<>();
        type.put("申通","shentong" );
        type.put("EMS","ems" );
        type.put("顺丰","shunfeng" );
        type.put("圆通","yuantong" );
        type.put("中通","zhongtong" );
        type.put("韵达","yunda" );
        type.put("天天","tiantian" );
        type.put("汇通","huitongkuaidi" );
        type.put("全峰","quanfengkuaidi" );
        type.put("德邦","debangwuliu" );
        type.put("宅急送","zhaijisong" );
		type.put("邮政包裹","youzhengguonei" );
        return type;
    }
}
