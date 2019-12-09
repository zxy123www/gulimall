package com.atguigu.gmall0624.manageservice.mapper;

import com.atguigu.gmall0624.bean.BaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

//通用Mapper不支持多变查询
//需要自己书写SQL语句
public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo>{
    List<BaseAttrInfo> getattrInfoList(String catalog3Id);
}
