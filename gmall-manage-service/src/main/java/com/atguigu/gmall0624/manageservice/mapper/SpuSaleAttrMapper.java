package com.atguigu.gmall0624.manageservice.mapper;

import com.atguigu.gmall0624.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);
}
