package com.atguigu.gmall0624.bean;

import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;


@Data
public class SpuInfo implements Serializable {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String spuName;

    @Column
    private String description;

    @Column
    private  String catalog3Id;

    //商品的属性集合
   @Transient
    private  List<SpuSaleAttr> spuSaleAttrList;

   //商品图片的集合
    private  List<SpuImage> spuImageList;



}
