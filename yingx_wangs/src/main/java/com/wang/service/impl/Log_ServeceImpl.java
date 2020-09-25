package com.wang.service.impl;


import com.wang.annotation.AddLog;
import com.wang.dao.LogDao;
import com.wang.entity.yx_Category;
import com.wang.entity.yx_Log;
import com.wang.entity.yx_User;
import com.wang.service.LogService;
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
 * @ClassName: Log_ServeceImpl
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service.impl
 * @Author:wang
 * @Date: 2020/8/31——22:57
 * @Description: TOOO
 */
@Service("LogService")
@Transactional
public class Log_ServeceImpl implements LogService {
    @Resource
    LogDao logDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)

    public HashMap<String, Object> queryLogPage(Integer rows, Integer page) {
        /*
         * rows 每页展示多少条          页面传递时为 当前页 后台返回时 为查询出来的数据
         * page 当前第几页
         * */
        HashMap<String, Object> Map = new HashMap<>();
        //查询总条数
        yx_Log log = new yx_Log();
        Integer count = logDao.selectCount(log);
        System.out.println("总条数--------" + count);
        //计算总页数
        Integer total = count % rows == 0 ? count / rows : count / rows + 1;
        //计算起始条数
        Integer star = (page - 1) * rows;  //起始条数
        System.out.println("起始条数----" + star);
        //分页查询信息
        // 第一个参数  ：起始的下标     第二个参数 ：每页展示多少条
        RowBounds rs = new RowBounds(star, rows);//查询条件对象
        Example e = new Example(yx_Log.class);  //yx_log类型 条件对象
        e.setOrderByClause("date_time DESC"); // 添加排序条件，etid必须是对应数据库表中的某个字段，多个字段则用逗号隔开 DESC降序
        List<yx_Log> users = logDao.selectByExampleAndRowBounds(e, rs);//查询数据类型  查询条件


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
}
