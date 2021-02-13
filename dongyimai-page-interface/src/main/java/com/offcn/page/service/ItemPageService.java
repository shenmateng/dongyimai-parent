package com.offcn.page.service;

//商品详细页接口
public interface ItemPageService {


    //生成商品详细页
    boolean genItemHtml(Long goodsId);

    //删除商品详细页
    boolean deleteItemHtml(Long[] goodsIds);
}
