package com.wang.controller;

import com.wang.entity.yx_Admin;
import com.wang.service.AdminService;
import com.wang.util.ImageCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * @ClassName: Admin_Controller
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.controller
 * @Author:wang
 * @Date: 2020/8/24——18:54
 * @Description: TOOO
 */
@Controller

/*
 * @Controller 标注为Controller层
 *
 * @RequestMapping("admin")对外访问路径
 *
 *
 * */
@RequestMapping("admin")
public class Admin_Controller {
    @Resource
    private AdminService adminService;

    //获取验证码
    @RequestMapping("code")  //对外方法的访问路径的

    public void code(HttpSession session, HttpServletResponse response)throws IOException {
        //获取yzm
        String yzm = ImageCodeUtil.getSecurityCode();
        System.out.println("验证码为:"+yzm);
        //存入session
        session.setAttribute("yzm", yzm);
        //获取验证码图片
        BufferedImage image = ImageCodeUtil.createImage(yzm);
        //获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        //验证码图片输出到页面
        ImageIO.write(image, "png", outputStream);
    }

    //管理员登录
    @RequestMapping("login")
    @ResponseBody
    public HashMap<String,Object> login(yx_Admin admin, String code){
        System.out.println(admin);
        System.out.println(code);

        HashMap<String, Object> map = adminService.login(admin, code);

        return map;
    }
}
