package com.wang.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @ClassName: yx_Admin
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.entity
 * @Author:wang
 * @Date: 2020/8/24——18:07
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "yx_admin")
/*通用mapper 第五步 (最后一步骤 )
*@Table(name = "t_user") 写在类上 指定实体类映射的数据库的表名
*
*
* @Id    写在属性上 表示这个属性对应的是数据库表的主键字段
* @Column(name="user_name")  写在属性上 如果实体属性名与数据库字段不一致，表示这个属性映射的数据库字段名
* @Transient     写在属性上  表示该属性在数据库不存在
*
* */
public class yx_Admin implements Serializable {
    @Id
    private String id;
    private String username;
    private String password;
}
