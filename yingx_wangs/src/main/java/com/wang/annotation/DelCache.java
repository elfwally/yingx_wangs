package com.wang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: DelCache
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.annotation
 * @Author:wang
 * @Date: 2020/9/7——22:18
 * @Description: TOOO
 */
//自定义注解
@Target(ElementType.METHOD)//设置指定当前注解应用在方法上     可以应用在那些位置  可以是 类  方法  属性 参数
@Retention(RetentionPolicy.RUNTIME)//用于指定注解的生命周期  适用范围 运行时生效
//@Documented  指定注解是否出现编译后的api中  一般不用
//@Inherit 注解是否可以被继承   一般不用
public @interface DelCache {
}
