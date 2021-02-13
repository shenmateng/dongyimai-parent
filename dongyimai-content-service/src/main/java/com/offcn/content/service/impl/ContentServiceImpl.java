package com.offcn.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.offcn.mapper.TbContentMapper;
import com.offcn.pojo.TbContent;
import com.offcn.pojo.TbContentExample;
import com.offcn.pojo.TbContentExample.Criteria;
import com.offcn.content.service.ContentService;

import com.offcn.entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */

    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */

    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */

    public void add(TbContent content) {
        contentMapper.insert(content);
        //新增完成清理缓存 保证数据一致性   缓存实现方法：FIFO LRU   <cache/>
        this.redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }


    /**
     * 修改
     */

    public void update(TbContent content) {
        //查询修改前的分类Id
        Long categoryId = this.contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        this.redisTemplate.boundHashOps("content").delete(categoryId);
        contentMapper.updateByPrimaryKey(content);
        //如果分类ID发生了修改,清除修改后的分类ID的缓存
        if (categoryId.longValue() != content.getCategoryId().longValue()) {
            this.redisTemplate.boundHashOps("content").delete(categoryId);
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */

    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */

    public void delete(Long[] ids) {
        for (Long id : ids) {
            //清除缓存
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();//广告分类ID
            redisTemplate.boundHashOps("content").delete(categoryId);
            contentMapper.deleteByPrimaryKey(id);
        }
    }


    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }
        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据分类ID查询广告
     *
     * @param categroyId
     * @return
     */
    public List<TbContent> findByCategoryId(Long categroyId) {
        //1.先从缓存中查询
        List<TbContent> contentList = (List<TbContent>) this.redisTemplate.boundHashOps("content").get(categroyId);
        //根据广告分类ID查询广告列表
        if (contentList == null) {
            //2.从数据库中查询 往缓存中放一份
            TbContentExample example = new TbContentExample();
            TbContentExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categroyId);
            //要求必须时开启状态
            criteria.andStatusEqualTo("1"); //
            example.setOrderByClause("sort_order");//排序显示
            contentList = this.contentMapper.selectByExample(example);
            this.redisTemplate.boundHashOps("content").put(categroyId, contentList); //存入到缓存中
            System.out.println("从数据库中读取数据");
        } else {
            System.out.println("从缓存中读取数据");
        }
        return contentList;
    }

}
