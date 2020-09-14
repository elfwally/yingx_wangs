package com.wang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//自定义注解
@Target(ElementType.METHOD)//设置指定当前注解应用在方法上     可以应用在那些位置  可以是 类  方法  属性 参数 
@Retention(RetentionPolicy.RUNTIME)//用于指定注解的生命周期  适用范围 运行时生效
//@Documented  指定注解是否出现编译后的api中  一般不用
//@Inherit 注解是否可以被继承   一般不用
public @interface AddLog { //声明自定义注解  注解名就是   @AddLog
    String value(); //注解中的值（可有可无  有用的时候 例@AddLog(value="写下想写的信息")）  
    String name() default ""; //注解中的名字 设置不写时默认为“”
}
