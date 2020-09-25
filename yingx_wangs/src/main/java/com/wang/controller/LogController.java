package com.wang.controller;

import com.wang.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: LogController
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.controller
 * @Author:wang
 * @Date: 2020/8/31——23:40
 * @Description: TOOO
 */
@Controller
@RequestMapping("log")
public class LogController {
    @Resource
    LogService logService;
    @RequestMapping("queryLogPage")
    @ResponseBody
    public HashMap<String,Object> quer(Integer rows,Integer page){
        return logService.queryLogPage(rows, page);
    }
}
