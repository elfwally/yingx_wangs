package com.wang.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//因为前台返回的全为 这三个数据  所以可以封装成一个对象 直接调用此对象进行数据返回 给前台
public class ResultCommon {
    private Object data;//返回的数据
    private String message;//返回的信息
    private String status;//返回的状态
}
