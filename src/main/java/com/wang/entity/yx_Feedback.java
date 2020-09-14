package com.wang.entity;

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
 * @ClassName: yx_Feedback
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.entity
 * @Author:wang
 * @Date: 2020/8/30——12:02
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "yx_feedback")
public class yx_Feedback implements Serializable {
    @Id
    private String id;
    private String title;
    private String content;
    @Column(name = "user_id")
    private String userID;
    @Column(name = "save_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saveDate;

}
