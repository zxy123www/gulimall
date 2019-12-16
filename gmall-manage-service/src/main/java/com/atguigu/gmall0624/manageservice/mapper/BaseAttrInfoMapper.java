package com.atguigu.gmall0624.manageservice.mapper;

import com.atguigu.gmall0624.bean.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

//通用Mapper不支持多变查询
//需要自己书写SQL语句
public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo>{

    //通过三级分类Id查询数据
    List<BaseAttrInfo> getattrInfoList(String catalog3Id);
    //通过平台属性值Id查询数据
    // 通过平台属性值Id 查询数据 171,81,120,167,82,83 ...
    List<BaseAttrInfo> selectAttrInfoListByIds(@Param("valueIds") String valueIds);
}
