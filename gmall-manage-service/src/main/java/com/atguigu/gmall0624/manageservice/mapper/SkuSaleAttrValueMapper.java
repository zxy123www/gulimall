package com.atguigu.gmall0624.manageservice.mapper;

import com.atguigu.gmall0624.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuSaleAttrValueMapper  extends Mapper<SkuSaleAttrValue> {
    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) ;
}
