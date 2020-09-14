package com.wang;

import com.wang.dao.NewUserDao;
import com.wang.entity.new_user;
import com.wang.po.VideoPo;
import com.wang.service.VideoService;
import com.wang.vo.CityVo;
import com.wang.vo.VideoVo;
import com.wang.vo.newUserVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: voTest
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang
 * @Author:wang
 * @Date: 2020/9/1——22:30
 * @Description: TOOO
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YingxWangsApplication.class)
public class voTest {
    @Resource
    VideoService videoService;
    @Resource
    NewUserDao newUserDao;
    @Test
    public void testss(){
        List<VideoVo> videoPos = videoService.queryFirst();
        System.out.println(videoPos);
    }
    @Test
    public void testsss(){
        List<newUserVo> list = newUserDao.queryUsernum("男",2020);
        System.out.println("1111"+list);
    }
    @Test
    public void atestsss(){
        List<CityVo> list = newUserDao.queryCityrnum("男");
        System.out.println("1111"+list);
    }
}
