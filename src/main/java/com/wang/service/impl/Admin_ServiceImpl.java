package com.wang.service.impl;

import com.wang.dao.AdminDao;
import com.wang.entity.yx_Admin;
import com.wang.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @ClassName: Admin_ServiceImpl
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service.impl
 * @Author:wang
 * @Date: 2020/8/24——18:47
 * @Description: TOOO
 */
@Service("AdminService")
@Transactional
public class Admin_ServiceImpl implements AdminService {
    @Resource
    private AdminDao adminDao;
@Resource
HttpSession session;
    /*
        @Transactional 事务注解
    * 查询使用propagation = Propagation.SUPPORTS
      增删改一般使用 @Transactional(propagation = Propagation.REQUIRED)
    * */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public HashMap<String, Object> login(yx_Admin admin, String code) {
        HashMap<String, Object> map = new HashMap<>();
        //获取后台存储验证码
        String yzm = (String) session.getAttribute("yzm");
        //创建一个条件对象
        Example e = new Example(yx_Admin.class);
        //给定查询条件   根据对象名称查询一个对象数据
        e.createCriteria().andEqualTo("username", admin.getUsername());
        yx_Admin admin1 = adminDao.selectOneByExample(e);

        //判断验证码是否一致
        if (yzm.toLowerCase().equals(code.toLowerCase())) {

            //判断用户名是否存在
            if (admin1 == null) {
                //存入提示信息
               map.put("message", "用户不存在！");
            } else {
                if (admin.getPassword().equals(admin1.getPassword())) {
                    session.setAttribute("admin", admin1);
                    //存入登录成功标记
                    map.put("state",true);
                } else {
                    //存入提示信息
                    map.put("message","密码错误！");
                }
            }
        } else {
            //存入提示信息
           map.put("message","验证码错误！");
        }
        return map;
    }
}
