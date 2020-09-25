package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName: yx_Log
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.entity
 * @Author:wang
 * @Date: 2020/8/31——22:35
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "yx_log")
public class yx_Log {
    @Id
    private Integer id;
    @Column(name = "admin_login")
    private String admin;
    @Column(name = "option_aa")
    private String option;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone="GMT+8")//时差这样显示正确时间
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "date_time")
    private Date date;
    private String status;
}
