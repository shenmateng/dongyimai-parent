package com.offcn.search.service;

import com.offcn.pojo.TbItem;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
    /**
     * 搜索
     *
     * @param
     * @return
     */
    Map<String, Object> search(Map searchMap);

    //导入数据
    void importList(List<TbItem> itemList);

    /**
     * 删除数据
     */
    public void deleteByGoodsIds(List goodsIdList);
}
