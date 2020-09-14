package com.wang.controller;


import com.wang.service.newUserService;
import com.wang.vo.ArryCityVo;
import com.wang.vo.CityVo;
import com.wang.vo.newUserVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("echarts")
public class EchartsController {
    @Resource
    newUserService userService;

    @RequestMapping("queryUserNum")
    public HashMap<String, Object> queryUserNum() {
        /*
         * 新用户表     new_user
         * id name phone sex city create_date
         * 查询每个月份用户注册量
         * month(create_date):该函数获取当前日期的月份
         * select concat(month(create_date),'月'),count(id) from new_user where sex='男'
         *       group by month(create_date)
         * select concat(month(create_date),'月'),count(id) from new_user where sex='女'
         *      group by month(create_date)
         * */
        //根据月份 性别 查询数据
        HashMap<String, Object> map = new HashMap<>();
        List<newUserVo> vos = userService.queryUsernum("男", 2020);
        List<newUserVo> vos1 = userService.queryUsernum("女", 2020);
        map.put("month", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"));
        map.put("boys", vos);
        map.put("girls", vos1);//Arrays.asList(50, 20, 16, 30, 20, 10)
        return map;
    }

    @RequestMapping("queryUserCity")
    public ArrayList<Object> queryUserCity() {
        /*
         * 新用户表     new_user
         * id name phone sex city create_date
         * 查询各个省份用户注册量
         * select city,count(id) value from new_user where sex='男' group by city
         *
         * select city,count(id) value from new_user where sex='女' group by city
         * */
        ArrayList<Object> list = new ArrayList<>();
        List<CityVo> vos = userService.queryCityrnum("男");
        ArryCityVo vo = new ArryCityVo("男", vos);
        System.out.println("城市为！！" + vo);
        List<CityVo> vos1 = userService.queryCityrnum("女");
        ArryCityVo vo1 = new ArryCityVo("女", vos1);
        list.add(vo);
        list.add(vo1);
      /*  ArrayList<CityVo> c=new ArrayList<>();
        c.add(new CityVo("北京",40));
        c.add(new CityVo("上海",89));
        c.add(new CityVo("河南",30));
        c.add(new CityVo("四川",50));
        c.add(new CityVo("云南",60));
        SeriesVo vo=new SeriesVo("男生",c);
        ArrayList<CityVo> c1=new ArrayList<>();
        c1.add(new CityVo("湖南",40));
        c1.add(new CityVo("湖北",59));
        c1.add(new CityVo("河北",20));
        c1.add(new CityVo("新疆",50));
        c1.add(new CityVo("西藏",90));
        SeriesVo vo1=new SeriesVo("女生",c1);
        list.add(vo);
        list.add(vo1);*/
        return list;
    }

}
