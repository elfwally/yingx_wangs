package com.wang.service;

import com.wang.entity.new_user;
import com.wang.vo.CityVo;
import com.wang.vo.newUserVo;

import java.util.List;

/**
 * @ClassName: newUserService
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service
 * @Author:wang
 * @Date: 2020/9/6——15:41
 * @Description: TOOO
 */
public interface newUserService {
    //查询每个月份用户注册量  根据男 女 查询 每个月注册人数
    List<newUserVo> queryUsernum(String sex,Integer year);
    //查询 各个省份新增用户量  根据男女查询
    List<CityVo> queryCityrnum(String sex);


}
