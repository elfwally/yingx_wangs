package com.wang.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: yx_User
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.entity
 * @Author:wang
 * @Date: 2020/8/25——17:54
 * @Description: TOOO
 */
@Table(name = "yx_user")//通用mapper  对应的表的名字
@Data
@AllArgsConstructor
@NoArgsConstructor

public class yx_User  implements Serializable {
    @Id  //主键  必须注解
    @Excel(name = "编号")  //列名,支持name_id
    private String id;
    @Excel(name = "用户名")
    private String username;
    @Excel(name = "用户电话")
    private String phone;
    @Column(name = "head_img")
    @Excel(name = "用户头像",type = 2,width = 70,height = 60)//默认type为1   1是文本 2 是图片,3 是函数,10 是数字 默认是文本
    private String headimg;
    @Excel(name = "用户简介")
    private String sign;
    @Excel(name = "用户微信")
    private String wechat;
    @Excel(name = "用户状态")
    private String status;
    @Excel(name = "用户注册时间",format = "yyyy-MM-dd")//设置时间格式类型
    @Column(name = "create_date") //当实体类字段与库表字段不一致时 用此注解进行匹配  create_date表中字段
    /*
      时间格式转换
    *   注解@JsonFormat主要是后台到前台的时间格式的转换

        注解@DataFormAT主要是前后到后台的时间格式的转换
    * */
    @JsonFormat(pattern = "yyyy-MM-dd") //主要是后台到前台的时间格式的转换
    @DateTimeFormat(pattern = "yyyy-MM-dd")//主要是前后到后台的时间格式的转换
    private Date createdate;
}
