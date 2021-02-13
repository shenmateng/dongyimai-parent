package com.offcn.sellergoods.service;

import com.offcn.entity.PageResult;
import com.offcn.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<TbBrand> findAll();

    //返回分页列表
    public PageResult findPage(int pageNum, int pageSize);  //第一个参数当前页，第二个参数每页记录数

    //增加方法
    public void add(TbBrand brand);

    //修改回显
    public TbBrand findOne(Long id);

    //修改
    public void update(TbBrand brand);

    //批量删除
    public void delete(Long[] ids);

    //模糊查询
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize);

    /**
     * 品牌下拉框数据
     */
    List<Map> selectOptionList();


}
