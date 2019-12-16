package com.atguigu.gmall0624.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuLsInfo implements Serializable {

    //skuId
    String id;
    //商品sku的价格
    BigDecimal price;
    //商品名称
    String skuName;
    //商品的三级分类Id
    String catalog3Id;
    //商品的默认图片
    String skuDefaultImg;
    //商品的热度评分
    Long hotScore=0L;
    //商品的属性值得集合
    List<SkuLsAttrValue> skuAttrValueList;

}
