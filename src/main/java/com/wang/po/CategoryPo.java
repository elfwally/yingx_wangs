package com.wang.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: CategoryPo
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.po
 * @Author:wang
 * @Date: 2020/9/2——22:47
 * @Description: TOOO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPo {
    private String id;
    private String idcateName;  //类别名
    private String levels; //类别级别
    private String parentId;//上级类别id
    private List<CategoryPo> categoryList;//下级类别集合


}
