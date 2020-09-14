package com.wang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: ArryCityVo
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.vo
 * @Author:wang
 * @Date: 2020/9/6——20:21
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArryCityVo {
    private String title;
    private List<CityVo> city;
}
