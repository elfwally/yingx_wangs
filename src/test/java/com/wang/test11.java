package com.wang;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.wang.dao.AdminDao;
import com.wang.dao.CategoryDao;
import com.wang.dao.LogDao;
import com.wang.dao.UserDao;
import com.wang.entity.yx_Admin;
import com.wang.entity.yx_Category;
import com.wang.entity.yx_Log;
import com.wang.entity.yx_User;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

/**
 * @ClassName: test11
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang
 * @Author:wang
 * @Date: 2020/8/24——18:30
 * @Description: TOOO
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YingxWangsApplication.class)
public class test11 {
    @Resource
    AdminDao adminDao;
    @Resource
    UserDao userDao;
    @Resource
    CategoryDao categoryDao;
    @Resource
    LogDao logDao;

    @Test
    public void tesetz() {
        yx_Admin yx_admin = new yx_Admin();
        //yx_admin.setId("1");
        //yx_admin.setUsername("小明");
        // List<yx_Admin> select = adminDao.select(yx_admin);
        //System.out.println(select);
        //创建一个条件对象
        Example e = new Example(yx_Admin.class);
        //给定查询条件
        e.createCriteria().andEqualTo("username", "小明");
        yx_Admin yx_admin1 = adminDao.selectOneByExample(e);
        System.out.println(yx_admin1);
    }

    @Test
    public void tesez() {
        yx_User yx_user = new yx_User();
        /*yx_user.setId("6");
        int i = userDao.selectCount(yx_user);
        System.out.println(i);*/

        //分页对象   第一个参数  ：起始的下标     第二个参数 ：每页展示多少条
        RowBounds rs = new RowBounds(0, 2);
        List<yx_User> admins = userDao.selectByRowBounds(yx_user, rs);
        System.out.println(admins);
    }

    //类别查询
    @Test
    public void ss() {
        //创建条件对象
        Example e = new Example(yx_Category.class);
        //给定查询条件
        //查询一级类别 条件
        e.createCriteria().andEqualTo("levels", "一级");
        //根据一级类别id 查询二级类别 条件0
        //e.createCriteria().andEqualTo("parentId", 2);
        // e.createCriteria().andEqualTo("username", "小明");
        RowBounds rs = new RowBounds(0, 2);//查询条件对象

        List<yx_Category> list1 = categoryDao.selectByExampleAndRowBounds(e, rs);
        //List<yx_Category> list = categoryDao.selectByExample(e);
        //int i = categoryDao.selectCountByExample(e);
        System.out.println(list1);
    }

    //
    @Test
    public void sss() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4G9GXGJuWrMhYhtJBynj";
        String accessKeySecret = "PswrBWKWBanj06bcqvOnmSH9dKTFkX";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传Byte数组。
        byte[] content = "Hello OSS".getBytes();
        ossClient.putObject("<yourBucketName>", "<yourObjectName>", new ByteArrayInputStream(content));

// 关闭OSSClient。
        ossClient.shutdown();
    }
    @Test
    public void zzzzz(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4G9GXGJuWrMhYhtJBynj";
        String accessKeySecret = "PswrBWKWBanj06bcqvOnmSH9dKTFkX";
        String bucketName = "yingx-wangs";  //存储空间名
        String objectName = "photo/aaa.jpg";  //文件名  可以指定文件目录
        String localFile="D:\\Photo Wallpaper\\cartoon\\123.jpg";  //本地视频路径

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。 参数：Bucket名字，指定文件名，文件本地路径
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(localFile));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
    @Test
    public void  zz(){
       RowBounds rs=new RowBounds(0,8);//查询条件对象
        Example e = new Example(yx_Log.class);  //yx_log类型 条件对象
       e.setOrderByClause("date_time DESC"); // 添加排序条件，etid必须是对应数据库表中的某个字段，多个字段则用逗号隔开
        List<yx_Log> users = logDao.selectByExampleAndRowBounds(e,rs);//查询数据类型  查询条件
       System.out.println(users);



       // yx_Log yx_user = new yx_Log();
        /*yx_user.setId("6");
        int i = userDao.selectCount(yx_user);
        System.out.println(i);*/

        //分页对象   第一个参数  ：起始的下标     第二个参数 ：每页展示多少条
        //RowBounds rs = new RowBounds(0, 2);
       // List<yx_Log> admins = logDao.selectByRowBounds(yx_user, rs);
       // System.out.println(admins);
    }

}
