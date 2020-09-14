package com.wang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@tk.mybatis.spring.annotation.MapperScan("com.wang.dao")
/* 通用mapper 第四步
* 通用mapper 引入注解 MapperScan（）  注意是@tk.mybatis.spring.annotation 包下的注解
* 此注解必须要放在整合mvc中的扫描包MapperScan（）注解的上面
* */
@MapperScan("com.wang.dao")
/*
 * import org.mybatis.spring.annotation.MapperScan;
 * 为整合mvc的扫描dao
 * */
@SpringBootApplication

public class YingxWangsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YingxWangsApplication.class, args);
    }

}
