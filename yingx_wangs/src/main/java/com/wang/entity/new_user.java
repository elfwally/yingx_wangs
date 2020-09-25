package com.wang.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName: new_user
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.entity
 * @Author:wang
 * @Date: 2020/9/6——15:31
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "new_user")
public class new_user {//此实体类 为echarts 和goeasy 使用   根据数据展示统计图
    @Id
    private String id;
    private String name;
    private String phone;
    private String sex;
    private String city;
    @Column(name = "create_date")
    private Date date;
}
