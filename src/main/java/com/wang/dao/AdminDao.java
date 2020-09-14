package com.wang.dao;

import com.wang.entity.yx_Admin;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName: AdminDao
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.dao
 * @Author:wang
 * @Date: 2020/8/24——18:11
 * @Description: TOOO
 */
/*通用mapper 配置 第三步
* 通用mapper  需要dao集成mapper方法
* 注意导入的是 tk.mybatis.mapper.common.Mapper; 包下的 Mapper<里边为当前要操作的对象>
*
* */
public interface AdminDao extends Mapper<yx_Admin> {
    yx_Admin login(yx_Admin admin);
}
