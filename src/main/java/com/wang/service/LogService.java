package com.wang.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: LogService
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service
 * @Author:wang
 * @Date: 2020/8/31——22:55
 * @Description: TOOO
 */
public interface LogService {
    //分页查询
    HashMap<String, Object> queryLogPage(Integer rows, Integer page);
}
