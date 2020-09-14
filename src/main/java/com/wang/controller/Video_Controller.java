package com.wang.controller;



import com.wang.entity.yx_Video;
import com.wang.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("video")
public class Video_Controller {

    @Resource
    VideoService videoService;
//后台视频查询
    @ResponseBody
    @RequestMapping("queryAllPage")
    public HashMap<String, Object> queryAllPage(Integer page, Integer rows) {
        return videoService.queryAllPage(page, rows);
    }

//后台视频操作
    @ResponseBody
    @RequestMapping("edit")
    public Object edit(yx_Video video, String oper) {

        if (oper.equals("add")) {
            return videoService.add(video);
        }

        if (oper.equals("edit")) {
            videoService.update(video);
        }

        if (oper.equals("del")) {
            HashMap<String, Object> map = videoService.delete(video);
            return map;
        }

        return null;
    }
    //后台视频上传阿里云服务器
    @ResponseBody
    @RequestMapping("uploadVdieo")
    public void uploadVdieo(String id,MultipartFile path,  HttpServletRequest request) {
        videoService.uploadVoidAliyun(id, path, request);
    }

    @ResponseBody
    @RequestMapping("searchVideo")
    public List<yx_Video> searchVideo(String content) {
        List<yx_Video> videos = videoService.searchVideos(content);
        return videos;

        /*
        视频模块

        增
           数据添加到mysql
           上传视频   aliyun
           截取封面
           上传封面
           修改数据

           添加索引  es
        删
           删除数据 mysql
           删除文件 aliyun
           删除索引 es
        改
           修改数据
           修改文件
           修改索引
        查
           mysql
           es 检索
           redis 缓存

           第一次查询   没有缓存
           添加缓存到  redis
           后期查询数据  redis
           检索   es
        * 基于阿里云实现视频模块的文件存储
          使用Elasticsearch,并结合ik分词器做视频的检索
          使用Spring AOP结合Reids实现项目中的通用缓存
        * */

    }

}
