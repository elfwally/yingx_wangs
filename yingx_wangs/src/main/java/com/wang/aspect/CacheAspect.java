package com.wang.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @ClassName: CacheAspect
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.aspect
 * @Author:wang
 * @Date: 2020/9/7——21:51
 * @Description: TOOO
 */
@Aspect//定义切面
@Configuration//交给spring工厂管理对象
public class CacheAspect { //添加redis缓存
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    //添加缓存
   // @Around("@annotation(com.wang.annotation.AddCache)")
//添加环绕通知   +  ()  切入点是自定义注解    组成切面    编织  AOP编程   //思路和 日志一样 可以通过切入点为方法 类 进行切面编程 但由于每个命名规范问题 此方法不通用  所以采用切自定义注解的方式  然后再需要的方法上加入自定义注解从而进行对加入自定义注解的方法进行切入实现更通用的切入点aop切面编程
    public Object addRedisCache(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("===环绕通知= 加入缓存==");

        /*序列化解决乱码*/                               //redis 需要对象序列化  既对象 实现 implements Serializable
        StringRedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);


        StringBuilder sb = new StringBuilder();//可拼接string

        //K=包名+类名+方法名+实参   ,   V=数据

        //获取类的全限定名  com.baizhi.serviceImpl.CategoryServiceImpl
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        sb.append(className);//字符串拼接全限定名

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);//拼接方法名

        //获取实参
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            //拼接参数
            sb.append(arg);
        }

        //获取key  com.baizhi.serviceImpl.UserServiceImpl.queryAllByPage.1.2
        String key = sb.toString();

        ValueOperations string = redisTemplate.opsForValue();

        //判断缓存中是否有数据  返回boolean turn 有数据  false没有数据
        Boolean aBoolean = redisTemplate.hasKey(key);

        Object result = null;
        if (aBoolean) {
            //有数据   取出数据返回结果
            result = string.get(key);
        } else {
            //没有数据   查询数据库获取结果 加入缓存
            //放行方法  查询数据库
            try {
                result = proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //获取方法得返回结果   加入缓存
            string.set(key, result);
        }
        return result;
    }

    //清除缓存
   // @After("@annotation(com.wang.annotation.DelCache)") //后置通知 再执行完增删改操作后 进行删除redis key  从而是redis中缓存的数据是最新的 再通过查数据的时候进行保存到redis中
    public void delCache(JoinPoint joinPoint) {

        //com.baizhi.serviceImpl.CategoryServiceImpl   queryAllByPage12
        //com.baizhi.serviceImpl.CategoryServiceImpl    queryAllByPage110
        //com.baizhi.serviceImpl.CategoryServiceImpl   queryAllByPage120

        //key=String,value=String
        //hash
        //KEY=(类的全限定名)String,  value=(key=(方法名+实参)String,value=(数据)String)

        //获取类的全限定名
        String className = joinPoint.getTarget().getClass().getName();

        //获取所有key  两种方式
        //Set keys1 = redisTemplate.keys("*");
        Set<String> keys = stringRedisTemplate.keys("*");
        //遍历所有key
        for (String key : keys) {
            //判断当前key是否是以className开头的
            if (key.startsWith(className)) {
                //删除key
                stringRedisTemplate.delete(key);
            }
        }
    }
}
