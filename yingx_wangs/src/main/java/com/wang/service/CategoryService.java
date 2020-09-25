package com.wang.service;

import com.wang.entity.yx_Category;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: CategoryService
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service
 * @Author:wang
 * @Date: 2020/8/26——14:07
 * @Description: TOOO
 */

public interface CategoryService {
    //父类别分页查询
    Map<String,Object> queryBypageParent(Integer page,Integer rows);

    //子类别分页查询
    Map<String,Object> queryBypageSub(Integer page,Integer rows,String row_id);
    //添加类别
    void add(yx_Category category);
    //修改类别
    void update(yx_Category category);
    //删除类别
    HashMap<String,Object> delete(yx_Category category);
}
