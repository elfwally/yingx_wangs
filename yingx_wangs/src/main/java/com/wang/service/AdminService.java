package com.wang.service;

import com.wang.entity.yx_Admin;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @ClassName: AdminService
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service
 * @Author:wang
 * @Date: 2020/8/24——18:44
 * @Description: TOOO
 */

public interface AdminService {
    //管理员登录
    HashMap<String ,Object> login(yx_Admin  admin, String code);
}
