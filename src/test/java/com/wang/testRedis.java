package com.wang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: testRedis
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang
 * @Author:wang
 * @Date: 2020/9/7——23:18
 * @Description: TOOO
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YingxWangsApplication.class)
public class testRedis {
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        /*序列化解决乱码*/                               //redis 需要对象序列化  既对象 实现 implements Serializable
        StringRedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        ValueOperations string = redisTemplate.opsForValue();
        string.set("name","小白");
        String name = (String) string.get("name");
        System.out.println(name);
       /* string.set("age","18",10, TimeUnit.SECONDS);

        for (int i = 0; i < 100; i++) {

            System.out.println(string.get("age"));

        }*/

    }

    @Test
    public void testRediss(){

        Set<String> keys = stringRedisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

    }
}
