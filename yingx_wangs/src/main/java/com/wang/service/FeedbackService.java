package com.wang.service;

import com.wang.entity.yx_Feedback;

import java.util.HashMap;

/**
 * @ClassName: FeedbackService
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service
 * @Author:wang
 * @Date: 2020/8/30——12:08
 * @Description: TOOO
 */
public interface FeedbackService {
    HashMap<String,Object> querypage(Integer rows,Integer page);
    void delete(yx_Feedback feedback);
}
