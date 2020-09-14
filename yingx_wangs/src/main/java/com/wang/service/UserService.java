package com.wang.service;

import com.wang.entity.yx_User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UserService
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service
 * @Author:wang
 * @Date: 2020/8/25——18:08
 * @Description: TOOO
 */
public interface UserService {
    //分页查询
    Map<String, Object> queryByPager(Integer rows, Integer page);

    //添加用户
    String  add(yx_User user);

    //查询用户
    yx_User queryByid(String id);
    //修改用户
    void update(yx_User user);
    //删除用户
    void delete(yx_User user);
    //用户头像上传阿里云服务器
    void uplodaAliyunheadImg(String id, MultipartFile path, HttpServletRequest request);
    //用户数据导出excel表格
    void ExportUser() throws IOException;
}
