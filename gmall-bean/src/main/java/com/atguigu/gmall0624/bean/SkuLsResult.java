package com.atguigu.gmall0624.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class SkuLsResult implements Serializable {
    //页面显示商品的集合
    List<SkuLsInfo> skuLsInfoList;
   //总页数
    long total;
    //总页数
    long totalPages;
    //平台属性值得Id集合
    //通过两张表的多表关联查询就可以查询平台属性以及平台的属性值
    List<String> attrValueIdList;
}
