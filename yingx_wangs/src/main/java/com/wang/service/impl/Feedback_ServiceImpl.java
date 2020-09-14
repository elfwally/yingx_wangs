package com.wang.service.impl;

import com.wang.annotation.AddCache;
import com.wang.annotation.AddLog;
import com.wang.annotation.DelCache;
import com.wang.dao.FeedbackDao;
import com.wang.entity.yx_Feedback;
import com.wang.entity.yx_User;
import com.wang.entity.yx_Video;
import com.wang.service.FeedbackService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: Feedback_ServiceImpl
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service.impl
 * @Author:wang
 * @Date: 2020/8/30——12:10
 * @Description: TOOO
 */
@Service("FeedbackService")
@Transactional
public class Feedback_ServiceImpl implements FeedbackService {
    @Resource
    FeedbackDao feedbackDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    @AddLog(value = "查询反馈信息")
    @AddCache
    public HashMap<String, Object> querypage(Integer rows, Integer page) {
        /*
         * rows 每页展示多少条          页面传递时为 当前页 后台返回时 为查询出来的数据
         * page 当前第几页
         * */
        HashMap<String, Object> Map = new HashMap<>();
        //查询总条数
        //总条数   records
        Example e = new Example(yx_Feedback.class);//创建类型对象
        int count = feedbackDao.selectCountByExample(e);
        System.out.println("总条数--------"+count);
        //计算总页数
        Integer total= count % rows == 0 ? count / rows : count / rows + 1;
        //计算起始条数
        Integer star=(page-1)*rows;  //起始条数
        System.out.println("起始条数----"+star);
        //分页查询信息
        // 第一个参数  ：起始的下标     第二个参数 ：每页展示多少条
        RowBounds rs=new RowBounds(star,rows);//查询条件对象
        List<yx_Feedback> users = feedbackDao.selectByRowBounds(new yx_Feedback(), rs);//查询数据类型  查询条件
        //List<yx_User> users = userDao.selectByExampleAndRowBounds(user, rs);  //查询数据类型  查询条件
        //查出的数据存入map   存入map的名字固定  （必须和jqgrid datatype类型名字一致 否则jqgrid 读取不到数据）
        /*
         * datatype : "json",    //响应  拿到的返回值？page页码   rows当前页的数据  total总页数    records总条数
         *
         * */
        Map.put("records", count);//总条数  records名字固定
        Map.put("total", total);//总页数  total名字固定
        Map.put("rows", users);//查询出的信息  rows名字固定
        Map.put("page", page);//当前页  page名字固定
        return Map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    @AddLog(value = "删除反馈信息")
    @DelCache
    public void delete(yx_Feedback feedback) {
        feedbackDao.deleteByPrimaryKey(feedback);
    }
}
