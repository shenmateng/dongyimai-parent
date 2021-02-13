package com.offcn.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.pojo.TbItem;
import com.offcn.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

@Component
public class ItemSearchListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    //监听端口 面向接口编程  面向抽象编程
    public void onMessage(Message message) {
        try {
            System.out.println("监听到消息");
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            List<TbItem> list = JSON.parseArray(text, TbItem.class);
            this.itemSearchService.importList(list);
            System.out.println("成功导入索引");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
