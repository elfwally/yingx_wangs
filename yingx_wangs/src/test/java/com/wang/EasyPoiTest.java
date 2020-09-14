package com.wang;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.wang.entity.yx_User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @ClassName: EasyPoi
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang
 * @Author:wang
 * @Date: 2020/9/2——22:32
 * @Description: TOOO
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YingxWangsApplication.class)
public class EasyPoiTest {
    @Test
    public void ss()throws  Exception{
        ImportParams params = new ImportParams();
        //设置标题的行数，有标题时一定要有
        params.setTitleRows(1);
        //设置表头的行数
        params.setHeadRows(1);
        //String file = Thread.currentThread().getContextClassLoader().getResource("D:\\用户信息.xls").getFile();
        /* List<yx_User> list = ExcelImportUtil.importExcel(
                new File(file),
                yx_User.class, params);*/
        FileInputStream stream=new FileInputStream(new File("D:\\用户信息.xls"));//上传的文件
        List<yx_User> list = ExcelImportUtil.importExcel(stream, yx_User.class, params);//上传的文件  导入数据的实体类  ImportParams类型对象

        System.out.println("解析到的数据长度是：" + list.size());
        for (yx_User scoreIssueReqPOJO : list) {
            System.out.println("***********有标题有表头导入的数据是=" + scoreIssueReqPOJO.toString());
        }
    }
}
