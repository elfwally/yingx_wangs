package com.wang.service.impl;

import com.wang.annotation.AddCache;
import com.wang.annotation.AddLog;
import com.wang.annotation.DelCache;
import com.wang.dao.CategoryDao;
import com.wang.entity.yx_Category;
import com.wang.service.CategoryService;
import com.wang.util.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: Category_ServiceImpl
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service.impl
 * @Author:wang
 * @Date: 2020/8/26——14:12
 * @Description: TOOO
 */
@Service("CategoryService")
@Transactional
public class Category_ServiceImpl implements CategoryService {
    @Resource
    CategoryDao categoryDao;

    //查找一级类别
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    @AddLog(value = "查询一级类别")
    @AddCache
    public Map<String, Object> queryBypageParent(Integer page, Integer rows) {  //当前页  每页展示多少条
        HashMap<String, Object> map = new HashMap<>();

        //起始条数
        int star = (page - 1) * rows;
        //总条数
        //创建条件对象
        Example e = new Example(yx_Category.class);  //yx_Category类型 条件对象
        //给定查询条件
        //查询一级类别 条件
        e.createCriteria().andEqualTo("levels", "一级");  //按levels属性字段 值为 ”一级“的查询有条件
        int count = categoryDao.selectCountByExample(e);//按条件查询总条数
        //总页数
        int total = count % rows == 0 ? (count / rows) : (count / rows + 1);
        //查询数据
        //创建条件对象
        // 第一个参数  ：起始的下标     第二个参数 ：每页展示多少条
        RowBounds rs = new RowBounds(star, rows);//查询条件对象  起始位置  每页条数

        List<yx_Category> list = categoryDao.selectByExampleAndRowBounds(e, rs);//查询数据类型  查询条件
        System.out.println("查出的一级类别---" + list);
        //存入map集合
        map.put("records", count);//总条数  records名字固定
        map.put("total", total);//总页数  total名字固定
        map.put("rows", list);//查询出的信息  rows名字固定
        map.put("page", page);//当前页  page名字固定

        return map;
    }

    //查找二级类别
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    @AddLog(value = "查询二级类别")
    @AddCache
    public Map<String, Object> queryBypageSub(Integer page, Integer rows, String rowId) {
        HashMap<String, Object> map = new HashMap<>();

        //起始条数
        int star = (page - 1) * rows;
        //总条数
        //创建条件对象
        Example e = new Example(yx_Category.class);
        //给定查询条件
        //查询二级类别 条件
        e.createCriteria().andEqualTo("parentId", rowId); //按parentId属性字段 值为传过来的的一级ID的值 进行查询
        int count = categoryDao.selectCountByExample(e);
        //总页数
        int total = count % rows == 0 ? (count / rows) : (count / rows + 1);
        //查询数据
        //创建条件对象
        // 第一个参数  ：起始的下标     第二个参数 ：每页展示多少条
        RowBounds rs = new RowBounds(star, rows);//查询条件对象
        List<yx_Category> list = categoryDao.selectByExampleAndRowBounds(e, rs);//查询数据类型  查询条件
        System.out.println("查出  的二级类别------" + list);
        //存入map集合
        map.put("records", count);//总条数  records名字固定
        map.put("total", total);//总页数  total名字固定
        map.put("rows", list);//查询出的信息  rows名字固定
        map.put("page", page);//当前页  page名字固定

        return map;

    }

    @Override
    public void update(yx_Category category) {

    }

    //删除类别
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AddLog(value = "删除类别")
    @DelCache
    public HashMap<String, Object> delete(yx_Category category) {
        HashMap<String, Object> map = new HashMap<>();
        //判断删除一级类别还是二级类别
        if (category.getParentId() != null) {
            //二级类别
            //判断二级类别下是否有视频
            if (true) {
                //有 删除失败 加提示信息
                categoryDao.deleteByPrimaryKey(category);
                map.put("status", "200");
                map.put("message", "删除成功");
            } else {
                //没有  删除成功 添加提示信息
            }
        } else {
            //一级类别
            //一级类别判断下面是否有二级类别
            System.out.println(category+"---------类别数据");
            Example e = new Example(yx_Category.class);//创建类型对象
            e.createCriteria().andEqualTo("parentId", category.getId());  //查询条件  根据一级类别id查询是否有二级类别条数
            int i = categoryDao.selectCountByExample(e);//查询二级类别个数
            System.out.println("查询的一级类别下二级类别的个数：------------"+i);
            //判断一级类别下是否有二级类别
            if (i != 0) {
                //有 删除失败 加提示信息
                map.put("status", "400");
                map.put("message", "删除失败，该类别下有子类别");
            } else {
                //没有  删除成功 添加提示信息
                categoryDao.deleteByPrimaryKey(category);
                map.put("status", "200");
                map.put("message", "删除成功");
            }
        }
        return map;
    }

    //添加类别
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AddLog(value = "添加类别")
    @DelCache
    public void add(yx_Category category) {
        //判断parentId 是否为null  null为一级类别 否则为二级类别
        if (category.getParentId() != null) {
            //二级类别
            category.setLevels("二级");
        } else {
            //一级类别
            category.setLevels("一级");
        }
        category.setId(UUIDUtil.getUUID());
        categoryDao.insertSelective(category);
    }
}
