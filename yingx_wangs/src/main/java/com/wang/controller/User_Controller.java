package com.wang.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.sun.net.httpserver.HttpContext;
import com.wang.annotation.AddLog;
import com.wang.entity.yx_User;
import com.wang.service.UserService;
import com.wang.util.AliyunPhoneSendUtil;
import com.wang.util.ImportExcel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName: User_Controller
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.controller
 * @Author:wang
 * @Date: 2020/8/25——20:22
 * @Description: TOOO
 */
@Controller
@RequestMapping("user")
public class User_Controller {
    @Resource
    UserService userService;
    //分页查询
    @RequestMapping("querypage")
    @ResponseBody
    public Map<String, Object> querypage(Integer rows, Integer page) {
        System.out.println(rows + "---------" + page);
        Map<String, Object> map = userService.queryByPager(rows, page);
        return map;
    }
    //插入数据
    @RequestMapping("edit")
    @ResponseBody
    public HashMap<String,Object> insert(yx_User yx_user,String oper){

        System.out.println("插入数据-----"+yx_user);
        HashMap<String, Object> Map = new HashMap<>();
        System.out.println("传入对象的信息为--------"+yx_user);
        if ("add".equals(oper)){
            String id = userService.add(yx_user);
            Map.put("id", id);
        }
        if ("edit".equals(oper)){
            userService.update(yx_user);
        }
        if ("del".equals(oper)){
            userService.delete(yx_user);
        }
        return Map;
    }


    //插入数据后 修改图像路径 保存图像
    @RequestMapping("uploadheadimg")
    @ResponseBody

    public void uploadheadimg(String id, MultipartFile headimg, HttpServletRequest request){
       userService.uplodaAliyunheadImg(id, headimg, request);

        /* //文件上传    本地
        //1.获取绝对路径
        String path=request.getSession().getServletContext().getRealPath("/bootstrap/img");//图片存储位置
        //2.文件上传
        //System.currentTimeMillis() 获取时间戳
        System.out.println(path);
        System.out.println(id);
        System.out.println(headimg);
        String filename = headimg.getOriginalFilename();
        System.out.println(filename);
        String cover=System.currentTimeMillis()+"_"+headimg.getOriginalFilename();
        System.out.println("图片路径--------"+cover);
        try {
            headimg.transferTo(new File(path,cover));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //修改图像路径
        yx_User user = userService.queryByid(id);

        user.setHeadimg(cover);
        System.out.println("修改后的对象-------"+user);
        userService.update(user);*/
    }
    //设置用户状态
    @RequestMapping("update")
    public void update(String id){
        System.out.println(id);
        yx_User user = userService.queryByid(id);
        System.out.println(user);
        String status = user.getStatus();
        if ("正常".equals(status)){
            user.setStatus("冻结");

        }else {
            user.setStatus("正常");
        }
        userService.update(user);

    }
    //后台导出用户数据 excl表格
    @AddLog(value = "后台导出用户数据")
    @RequestMapping("ExportUser")
    @ResponseBody
    public HashMap<String, Object> ExportUser()throws IOException{
        HashMap<String, Object> map = new HashMap<>();
            userService.ExportUser();
           map.put("message", "成功！");
           return map;
    }
    //后台导入excel表格
    @AddLog(value = "后台导入用户数据")
    @RequestMapping("ImportUser")
    @ResponseBody
    public HashMap<String,Object> Import(MultipartFile files, HttpSession session) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        String filename = files.getOriginalFilename();
        System.out.println("导入文件名称"+filename);
        System.out.println("导入的文件：1111"+files);
        ImportParams params = new ImportParams();
        //设置标题的行数，有标题时一定要有
        params.setTitleRows(1);
        //设置表头的行数
        params.setHeadRows(1);
        FileInputStream stream=new FileInputStream(new File("D:\\"+filename));//上传的文件
        List<yx_User> list = ExcelImportUtil.importExcel(stream, yx_User.class, params);//上传的文件  导入数据的实体类  ImportParams类型对象


        System.out.println("解析到的数据长度是：" + list.size());
        if (list.size()!=0){
            for (yx_User scoreIssueReqPOJO : list) {
                System.out.println("***********有标题有表头导入的数据是=" + scoreIssueReqPOJO.toString());
            }
            map.put("message", "导入成功！");

        }else {
            map.put("message", "导入失败！");

        }
        return  map;
    }


    //用户手机注册 获取验证码
    @RequestMapping("phoneSend")
    @ResponseBody
    public HashMap<String,String> phoneSend(String phone){
        HashMap<String, String> map = new HashMap<>();
        String randCode = AliyunPhoneSendUtil.getRandCode(6);//获取长度为6的随机数字的验证码
        String s = AliyunPhoneSendUtil.sendPhoneCode(phone, randCode);//返回发送结果信息 （成功 失败 停机。。）
        //正规是 把验证码保存到redis
        map.put("message", s);//存入发送结果信息
        map.put("code", randCode);//存入验证码
        System.out.println("手机号"+phone+"验证码为:"+randCode);
        return map;
    }
}
