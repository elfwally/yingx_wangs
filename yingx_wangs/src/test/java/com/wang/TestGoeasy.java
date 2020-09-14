package com.wang;

import com.alibaba.fastjson.JSON;
import io.goeasy.GoEasy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * @ClassName: TestGoeasy
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang
 * @Author:wang
 * @Date: 2020/9/6——21:44
 * @Description: TOOO
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YingxWangsApplication.class)
public class TestGoeasy {
    @Test
    public void sss(){//下面代码 正常是加到对数据库增删的servic代码中  然后页面引入相关配置  这样才会实现实时数据动态展示
        GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-eedfe1730ba14d7d9741c8d33048f862");//goeasy网址 项目名中详情 APPkeys 的Common key
        goEasy.publish("yingx_wang", "Hello, GoEasy!");//规定管道名称 随意      发送内容

    }

    @Test
    public void ssss(){//下面代码 正常是加到对数据库增删的servic代码中  然后页面引入相关配置  这样才会实现实时数据动态展示
        Random random=new Random();
        for(int i=0;i<5;i++){
            HashMap<String,Object> map=new HashMap<>();
            map.put("month", Arrays.asList("1月","2月","3月","4月","5月","6月"));
            map.put("boys", Arrays.asList(random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            map.put("girls", Arrays.asList(random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            //将map转换json格式的数据
            //发布消息的时机  数据库的数据发生改变的时候   增删改   实际应用中 上面的操作是对数据的增删操作 然后再在下面加入goeasy 的代码实现实时数据动态展示
            String s = JSON.toJSONString(map);
            GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-eedfe1730ba14d7d9741c8d33048f862");
            goEasy.publish("yingx_wang", s);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
