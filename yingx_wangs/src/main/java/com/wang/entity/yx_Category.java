package com.wang.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @ClassName: yx_Category
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.entity
 * @Author:wang
 * @Date: 2020/8/26——14:02
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "yx_category") //此注解为通用mapper  详看用户实体类
public class yx_Category implements Serializable {//实体类序列化 为redis缓存做准备  redis数据缓存需要对象序列化
    @Id
    private String id;
    private String name;
    private String levels;
    @Column(name = "parent_id")
    private String parentId;
}
