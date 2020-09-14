package com.wang.dao;

import com.wang.entity.new_user;
import com.wang.vo.CityVo;
import com.wang.vo.newUserVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: NewUserDao
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.dao
 * @Author:wang
 * @Date: 2020/9/6——15:34
 * @Description: TOOO
 */
public interface NewUserDao extends Mapper<new_user> {
    //查询每个月份用户注册量  根据男 女 查询 每个月注册人数
    List<newUserVo> queryUsernum(@Param("sex") String sex,@Param("create_date") Integer year);
    //查询 各个省份新增用户量  根据男女查询
    List<CityVo> queryCityrnum(String sex);

}
