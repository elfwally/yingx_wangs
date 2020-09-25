package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: yx_Video
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.entity
 * @Author:wang
 * @Date: 2020/8/27——22:37
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "yx_video")
//指定索引名称，索引类型
@Document(indexName = "yingx", type = "video")
public class yx_Video implements Serializable {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String biref; //视频简介

    @Field(type = FieldType.Keyword)
    private String path; //视频地址

    @Field(type = FieldType.Keyword)
    private String cover;  //封面地址

    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publishi_date")
    private Date publishiDate;  //发布时间

    @Field(type = FieldType.Keyword)
    @Column(name = "category_id")
    private String categoryId;  //二级类别ID

    @Field(type = FieldType.Keyword)
    @Column(name = "user_id")
    private String userID;

    @Field(type = FieldType.Keyword)
    @Column(name = "group_id")
    private String groupID;

    /*  关于ES注解
    * @Document`: 代表一个文档记录

​	`indexName`:  用来指定索引名称

​	`type`:		用来指定索引类型

    `@Id`: 用来将对象中id和ES中_id映射

    `@Field`: 用来指定ES中的字段对应Mapping

​	`type`: 用来指定ES中存储类型

​	`analyzer`: 用来指定使用哪种分词器
    *
    *
    * */
}
