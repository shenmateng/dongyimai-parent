package com.offcn.sms.service.impl;

import com.offcn.util.SmsUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class SmsMessageListenerImpl implements MessageListener {
    @Autowired
    private SmsUtil smsUtil;

    //收短信
    public void onMessage(Message message) {
        //MapMessage castException类型转换异常 向下转型不安全
        //不分离 js+java
        try {
            if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                String mobile = mapMessage.getString("mobile");
                String param = mapMessage.getString("param");
                HttpResponse response = smsUtil.sendSms(mobile, param);
                System.out.println("短信发送成功" + EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
