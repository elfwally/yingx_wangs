package com.wang.controller;

import com.wang.entity.yx_Feedback;
import com.wang.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @ClassName: Feedback_Controller
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.controller
 * @Author:wang
 * @Date: 2020/8/30——11:59
 * @Description: TOOO
 */
@Controller
@RequestMapping("feedback")
public class Feedback_Controller {
    @Resource
    FeedbackService feedbackService;

    //分页查询
    @RequestMapping("querypage")
    @ResponseBody
    public HashMap<String, Object> querypage(Integer rows, Integer page) {
        return feedbackService.querypage(rows, page);
    }
    //增删改反馈信息
    @RequestMapping("edit")
    @ResponseBody
    public void edit(yx_Feedback feedback,String oper){
        if ("del".equals(oper)){
            feedbackService.delete(feedback);
        }
    }

}
