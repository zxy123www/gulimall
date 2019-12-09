package com.atguigu.gmall0624.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0624.bean.*;
import com.atguigu.gmall0624.service.ManageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class SkuManageController {

    @Reference
    ManageService  manageService;
    //spuImageList
    @RequestMapping("spuImageList")
    public List<SpuImage>  spuImageList(SpuImage spuImage){
        List<SpuImage>  spuImageList=   manageService.getSpuImageList(spuImage);
        return  spuImageList;
    }
    //根据spuId查询销售属性
    //http://localhost:8082/spuSaleAttrList?spuId=63
    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr>   spuSaleAttrList(String spuId ){

        List<SpuSaleAttr> spuSaleAttrList=manageService.getSpuSaleAttrList(spuId);
        return  spuSaleAttrList;
    }

    //大保存
    //saveSkuInfo
    @RequestMapping("saveSkuInfo")
    public void   saveSkuInfo( @RequestBody SkuInfo skuInfo){
        manageService.saveSkuInfo( skuInfo);
    }

}
