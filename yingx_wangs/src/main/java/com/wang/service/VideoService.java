package com.wang.service;

import com.wang.entity.yx_Video;
import com.wang.po.VideoPo;
import com.wang.vo.VideoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: VideoService
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service
 * @Author:wang
 * @Date: 2020/8/27——22:46
 * @Description: TOOO
 */
public interface VideoService {
    //分页查询
    HashMap<String,Object> queryAllPage(Integer page, Integer rows);
    //视频上传
    String add(yx_Video video);
    //将视频数据上传至aliyun  java截取视频封面
    void uploadVoidAliyun(String id,MultipartFile path,HttpServletRequest request);
    //视频删除
    public HashMap<String,Object> delete(yx_Video video);
    //修改视频
    void update(yx_Video video);

    //查询首页视频信息
    List<VideoVo> queryFirst();
//索引视频
    List<yx_Video> searchVideo(String content);
//高亮查询索引视频
    List<yx_Video> searchVideos(String content);



























 /*   //将视频数据上传至本地
    void uploadVdieos(MultipartFile headImg, String id, HttpServletRequest request);

    //将视频数据上传至aliyun  java截取
    void uploadVdieosAliyun(MultipartFile path, String id, HttpServletRequest request);

    //将视频数据上传至aliyun  aliyun截取
    void uploadVdieosAliyuns(MultipartFile path, String id, HttpServletRequest request);

    void update(yx_Video video);

    HashMap<String, Object> delete(yx_Video video);

    //删除阿里云文件
    HashMap<String, Object> deleteAliyun(yx_Video video);*/

}
