package com.wang.controller;

import com.wang.entity.yx_Category;
import com.wang.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Category_Controller
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.controller
 * @Author:wang
 * @Date: 2020/8/26——18:42
 * @Description: TOOO
 */
@Controller
@RequestMapping("category")
public class Category_Controller {
    @Resource
    CategoryService categoryService;

    //查询父级类别
    @RequestMapping("category")
    @ResponseBody
    public Map<String, Object> category(Integer rows, Integer page) {
        Map<String, Object> map = categoryService.queryBypageParent(page, rows);
        System.out.println("------------当前条数："+rows+"---------当前页："+page);
        return map;
    }

    //查询子级类别
    @RequestMapping("twoCategory")
    @ResponseBody
    public Map<String, Object> category(Integer rows, Integer page, String id) {

        System.out.println(rows+"---------当前页："+page+"------------主类别ID"+id);
        Map<String, Object> map = categoryService.queryBypageSub(page, rows, id);
        return map;
    }
    //表格增删改 操作
    @RequestMapping("edit")
    @ResponseBody
    public HashMap<String,Object> edit(yx_Category category, String oper){
        HashMap<String,Object> map=null;
        System.out.println(category);
        if ("add".equals(oper)){
            System.out.println("页面传递一级类别对象：----"+category);
            categoryService.add(category);
        }
        if ("edit".equals(oper)){

        }
        if ("del".equals(oper)){
            map = categoryService.delete(category);
        }
        return map;
    }
}
