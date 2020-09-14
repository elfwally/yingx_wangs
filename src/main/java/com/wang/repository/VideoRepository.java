package com.wang.repository;


import com.wang.entity.yx_Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

//泛型   <操作对象类型,序列化主键的类型>
public interface VideoRepository extends ElasticsearchRepository<yx_Video,String> {

    List<yx_Video> findByTitle(String title);

    List<yx_Video> findByBiref(String biref);

}
