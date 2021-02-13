package com.offcn.util;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsUtil {
    @Value("${appcode}")
    private String appCode;
    @Value("${tpl}")
    private String tpl;
    //hashMap:动态扩容  链表
    private String host = "http://dingxin.market.alicloudapi.com";

    public HttpResponse sendSms(String mobile, String param) {
        HttpResponse response = null;
        try {
            String path = "/dx/sendSms";
            String method = "POST";
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "APPCODE " + appCode);//半角空格一个
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("mobile", mobile);
            querys.put("param", "code:" + param);
            System.out.println(tpl);
            querys.put("tpl_id", tpl);
            Map<String, String> bodys = new HashMap<String, String>();
            response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
