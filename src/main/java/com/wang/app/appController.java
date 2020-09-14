package com.wang.app;

import com.wang.common.ResultCommon;
import com.wang.po.VideoPo;
import com.wang.service.VideoService;
import com.wang.util.AliyunPhoneSendUtil;
import com.wang.vo.VideoVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: appController
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.app
 * @Author:wang
 * @Date: 2020/9/1——19:24
 * @Description: TOOO
 */

@RequestMapping("app")
@RestController  //等同于@ResponseBody ＋ @Controller合在一起的作用
public class appController {
    @Resource
    VideoService videoService;
//用户登录
    @RequestMapping("getPhoneCode")
    public ResultCommon getPhoneCode(String phone) {
        //获取随机验证码
        String randCode = AliyunPhoneSendUtil.getRandCode(6);//获取长度为6的随机验证码
        //保存验证码
        //发送短信验证
        String s = AliyunPhoneSendUtil.sendPhoneCode(phone, randCode);
        if ("发送成功".equals(s)) {
            return new ResultCommon(phone, "验证码发送成功", "100");
        }
        if ("发送失败".equals(s)) {
            return new ResultCommon(null, "验证码发送失败", "104");
        }
        return null;
    }
//首页数据查询
    @RequestMapping("queryByReleaseTime")
    public ResultCommon queryByReleaseTime() {
        List<VideoVo> videoVos = videoService.queryFirst();
        //判断如果list长度不为零则有数据
        if (videoVos.size()!=0){
            return new ResultCommon(videoVos, "查询成功", "100");
        }else {
            return new ResultCommon(null, "查询失败", "104");
        }
    }
}
