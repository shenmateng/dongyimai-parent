package com.offcn.solrutil;

import com.alibaba.fastjson.JSON;
import com.github.promeg.pinyinhelper.Pinyin;
import com.offcn.mapper.TbItemMapper;
import com.offcn.pojo.TbItem;
import com.offcn.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {
    //dao层接口
    @Autowired
    private TbItemMapper itemMapper;
    //注入搜索对象
    @Autowired
    private SolrTemplate solrTemplate;


    //从item表中查出已审核的商品
    public void importItemData() {
        //从Tb_item表中将数据读出 注意审核状态必须为1
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");// 已审核
        List<TbItem> itemList = itemMapper.selectByExample(example);
        System.out.println("===商品列表===");
        for (TbItem item : itemList) {
            System.out.println(item.getTitle() + "-----" + item.getPrice());
            //从数据库中查询到spec数据，并进行数据类型转换
            Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);
            //创建一个新map集合存储拼音
            Map<String, String> pinyinMap = new HashMap<String, String>();
            //遍历KEY值,替换key从汉字变为拼音
            for (String key : specMap.keySet()) {
                //将KEY值进行拼音转换，并重新将数据保存的到新的集合
                pinyinMap.put(Pinyin.toPinyin(key, "").toLowerCase(), specMap.get(key));
            }
            //重新设置到动态域
            item.setSpecMap(pinyinMap);
        }
        //保存集合数据到solr
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("导入成功");

    }

    public static void main(String[] args) {
        //手动获取容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();
//        solrUtil.testDel();

    }

    //删除solr的数据
    public void testDel() {
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


}
