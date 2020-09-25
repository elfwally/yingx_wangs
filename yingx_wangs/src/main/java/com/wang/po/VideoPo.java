package com.wang.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor//po对象  用于表现数据库中的一条记录映射成的Java对象
public class VideoPo {//视频的po对象 为数据库中查出的数据   因为点赞数 和视频播放数存在redis中 所以po对象中没有定义点赞数
    private String id;
    private String title;//视频标题
    private String cover;//视频封面
    private String path;//视频路径
    private Date publishDate;//视频长传时间
    private String brief;//视频描述
    private String cateName;//所属类别
    private String headImg;//用户头像

}
