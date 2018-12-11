package com.linghong.dongdong.controller;

import com.alibaba.fastjson.JSON;
import com.linghong.dongdong.dto.Response;
import com.linghong.dongdong.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 基础控制层
 */
@RestController
public class BaseController {
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Resource(name = "baseServiceImpl")
    private BaseService baseService;

    /**
     * 获取手机验证码
     *
     * @param mobilePhone
     * @return
     */
    @PostMapping("/api/getCode")
    public WebAsyncTask<Response> getCode(String mobilePhone) {
        Callable<Response> callable = new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                String code = baseService.getCode(mobilePhone);
                Response response = new Response();
                if (code != null) {
                    List<Object> data = new ArrayList<>();
                    String result = "{'result':" + code + "}";
                    data.add(JSON.parseObject(result));
                    response.set(true, 200, data, "手机验证码");
                } else {
                    response.set(false, 500, null, "获取手机验证码错误，请重试");
                }
                logger.info(JSON.toJSONString(response));
                return response;
            }
        };
        return new WebAsyncTask<>(1000 * 5, callable);
    }
}
