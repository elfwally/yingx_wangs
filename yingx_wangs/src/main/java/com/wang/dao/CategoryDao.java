package com.wang.dao;

import com.wang.entity.yx_Category;
import com.wang.po.CategoryPo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName: Category
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.dao
 * @Author:wang
 * @Date: 2020/8/26——14:06
 * @Description: TOOO
 */
public interface CategoryDao extends Mapper<yx_Category> {
    //查询类别
    List<CategoryPo> queryCategoryPo();
}
