package com.wang.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.wang.annotation.AddCache;
import com.wang.annotation.AddLog;
import com.wang.annotation.DelCache;
import com.wang.dao.UserDao;
import com.wang.entity.yx_User;
import com.wang.entity.yx_Video;
import com.wang.service.UserService;
import com.wang.util.AliyunOSSUtil;
import com.wang.util.InterceptVideoCoverUtil;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName: UserServiceImpl
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service.impl
 * @Author:wang
 * @Date: 2020/8/25——18:28
 * @Description: TOOO
 */
@Service("UserService")
@Transactional
public class User_ServiceImpl implements UserService {
    @Resource
    UserDao userDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    @AddLog(value = "查询用户信息")
    @AddCache
    public Map<String, Object> queryByPager(Integer rows, Integer page) {  //每页展示多少条  ，当前页
        /*
         * rows 每页展示多少条          页面传递时为 当前页 后台返回时 为查询出来的数据
         * page 当前第几页
         * */
        Map<String, Object> Map = new HashMap<>();
        //查询总条数
        yx_User user = new yx_User();
        Integer count = userDao.selectCount(user);
        System.out.println("总条数--------"+count);
        //计算总页数
       Integer total= count % rows == 0 ? count / rows : count / rows + 1;
       //计算起始条数
       Integer star=(page-1)*rows;  //起始条数
        System.out.println("起始条数----"+star);
        //分页查询信息
        // 第一个参数  ：起始的下标     第二个参数 ：每页展示多少条
        RowBounds rs=new RowBounds(star,rows);//查询条件对象
        List<yx_User> users = userDao.selectByRowBounds(user, rs);//查询数据类型  查询条件
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
    @AddLog(value = "更改用户状态")
    @DelCache
    public String add(yx_User user) {
        user.setId(UUID.randomUUID().toString());  //设置id 通过uuid
        user.setStatus("正常");
        user.setCreatedate(new Date());
        userDao.insertSelective(user);
        return user.getId();   //返回用户id  controller层 存入map 发送json  然后jqgrid插入数据后执行的afterSubmit 方法会用到用户id
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public yx_User queryByid(String id) {
        yx_User yxUser = userDao.selectByPrimaryKey(id);
        return yxUser;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AddLog(value = "更改用户信息")
    @DelCache
    public void update(yx_User user) {
        userDao.updateByPrimaryKeySelective(user);

    }



    //用户头像上传阿里云服务器
    @Override
    public void uplodaAliyunheadImg(String id, MultipartFile path, HttpServletRequest request) {
        //获取视频上传文件名  加上时间戳
        String filename = System.currentTimeMillis() + "_" + path.getOriginalFilename();
        System.out.println("头像文件名:" + filename);
        //进行远程文件上传  需要用到阿里云工具 byte上传
        AliyunOSSUtil.uploadFileByte(path, "yingx-wangs", "headImg/" + filename);//MultipartFile类型的文件    存储空间名(Bucket名称)    文件名
        //获取远程阿里云视频存放路径  https://Bucket名称.oss-cn-beijing.aliyuncs.com/文件名/
        String fetfilePath = "https://yingx-wangs.oss-cn-beijing.aliyuncs.com/headImg/" + filename;
        //修改数据库视频数据
        yx_User video = new yx_User();
        video.setId(id);
        //头像封面
        video.setHeadimg(fetfilePath);
        userDao.updateByPrimaryKeySelective(video); //通过主键进行修改  没有添加的属性 对数据库不操作
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AddLog(value = "删除用户信息")
    @DelCache
    public void delete(yx_User user) {
        userDao.deleteByPrimaryKey(user);
    }
    //导出用户数据  excel表格
    @Override
    @AddLog(value = "导出用户信息")
    public void ExportUser()throws IOException {
        //查询数据库数据
        List<yx_User> users = userDao.selectAll();
            //创建list集合 存储修改后的头像文件路径 然后调用excel 导出用户数据
        System.out.println("用户数据："+users);
        List<yx_User> list = new ArrayList<>();
        //获取图片名称
            for (yx_User u:users){
                //https://yingx-wangs.oss-cn-beijing.aliyuncs.com/headImg/1599033961073_1f9b4f541de99ffe616e738434ce0b2a.jpg
                //进行拆分  拆分了五段 https:  /  yingx-wangs.oss-cn-beijing.aliyuncs.com    headImg    1599033961073_1f9b4f541de99ffe616e738434ce0b2a.jpg
                String[] split = u.getHeadimg().split("/");
                //图片名称
                String headImgName = split[4];
                System.out.println("图片的文件名为："+headImgName);
                //设置存储本地位置
                String hedaImgPath ="D:\\code\\"+headImgName;
                System.out.println("图片的路径为："+hedaImgPath);
                //从阿里云服务器下载头像到本地
                AliyunOSSUtil.download("yingx-wangs","headImg/"+headImgName,hedaImgPath); //阿里云存储空间名  文件名  本地文件路径
                u.setHeadimg(hedaImgPath);//将头像文件路径改为本地路径
                list.add(u);//添加到新list
            }
        System.out.println("修改后的集合"+list);
        //文件导出 代码固定   //参数：(一级标题，二级标题，表名)，实体类类对象，导出的集合
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户数据","用户表"),
                yx_User.class, users);
        workbook.write(new FileOutputStream(new File("D:/用户信息.xls")));//导出文件位置 以及文件名称
        //释放资源
        workbook.close();
    }
}
