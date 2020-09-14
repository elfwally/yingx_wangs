package com.wang.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @ClassName: CacheAspectHash
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.aspect
 * @Author:wang
 * @Date: 2020/9/7——23:22
 * @Description: TOOO
 */
@Aspect
@Configuration//交给spring工厂管理对象
public class CacheAspectHash {
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    //添加缓存
    @Around("@annotation(com.wang.annotation.AddCache)")
    public Object addRedisCache(ProceedingJoinPoint proceedingJoinPoint){

        System.out.println("===环绕通知= 加入缓存==");

        /*序列化解决乱码*/
        StringRedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);

        //K=包名+类名+方法名+实参   ,   V=数据
        //KEY=类的全限定名,  value=(key=方法名+实参,value=数据)

        StringBuilder sb = new StringBuilder();

        //获取类的全限定名  com.baizhi.serviceImpl.CategoryServiceImpl
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);

        //获取实参
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            //拼接参数
            sb.append(arg);
        }

        //获取key  com.baizhi.serviceImpl.UserServiceImpl.queryAllByPage.1.2
        String key = sb.toString();

        //判断缓存中是否有数据
        HashOperations hash = redisTemplate.opsForHash();
        Boolean aBoolean = hash.hasKey(className, key);

        Object result =null;
        if(aBoolean){
            //有数据   取出数据返回结果
            result = hash.get(className,key);
        }else{
            //没有数据   查询数据库获取结果 加入缓存
            //放行方法  查询数据库
            try {
                result = proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //获取方法得返回结果   加入缓存
            hash.put(className,key,result);
        }
        return result;
    }

    //清楚缓存
    @After("@annotation(com.wang.annotation.DelCache)")
    public void delCache(JoinPoint joinPoint){
        System.out.println("===后置通知= 删除缓存==");
        //com.baizhi.serviceImpl.CategoryServiceImpl   queryAllByPage12
        //com.baizhi.serviceImpl.CategoryServiceImpl    queryAllByPage110
        //com.baizhi.serviceImpl.CategoryServiceImpl   queryAllByPage120

        //key=String,value=String
        //hash
        //KEY=(类的全限定名)String,  value=(key=(方法名+实参)String,value=(数据)String)

        //获取类的全限定名
        String className = joinPoint.getTarget().getClass().getName();

        Boolean b = stringRedisTemplate.delete(className);
        System.out.println("删除缓存："+b);

    }
}
