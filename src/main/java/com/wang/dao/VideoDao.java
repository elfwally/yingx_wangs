package com.wang.dao;

import com.wang.entity.yx_Video;
import com.wang.po.VideoPo;
import com.wang.vo.VideoVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName: VideoDao
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.dao
 * @Author:wang
 * @Date: 2020/8/27——22:44
 * @Description: TOOO
 */
//继承通用mapper接口
public interface VideoDao extends Mapper<yx_Video> {
    //首页查询视频
    List<VideoPo> queryByReleaseTime();//数据为数据库中查出的数据  所以必须是po对象 然后查出的po对象在赋值到vo对象返回给前端页面
}
