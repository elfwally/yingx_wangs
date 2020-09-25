package com.wang.service.impl;

import com.wang.dao.NewUserDao;
import com.wang.entity.new_user;
import com.wang.service.newUserService;
import com.wang.vo.CityVo;
import com.wang.vo.newUserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: newUserServiceImpl
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service.impl
 * @Author:wang
 * @Date: 2020/9/6——15:42
 * @Description: TOOO
 */
@Service("newUserService")
@Transactional
public class newUserServiceImpl implements newUserService {
    @Resource
    NewUserDao newUserDao;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<newUserVo> queryUsernum(String sex,Integer year) {
        return newUserDao.queryUsernum(sex,year);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CityVo> queryCityrnum(String sex) {
        return newUserDao.queryCityrnum(sex);
    }


}
