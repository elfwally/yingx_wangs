package com.wang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor//vo 对象   表现对象用于 前端页面展示
public class VideoVo {  //vo对象为页面前端展示的数据对象   所以会和接口文档提供的数据一致
    private String id;//视频id
    private String videoTitle;//视频标题
    private String cover;//视频封面
    private String path;//视频地址
    private String uploadTime;//视频上传时间
    private String description;//视频描述
    private Integer likeCount;//点赞数
    private String cateName;//所属类别名
    private String userPhoto;//用户头像
}
