package com.atguigu.gmall0624.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0624.bean.BaseSaleAttr;
import com.atguigu.gmall0624.bean.SpuImage;
import com.atguigu.gmall0624.bean.SpuInfo;
import com.atguigu.gmall0624.service.ManageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class SpuManageController {

    @Reference
    private ManageService manageService;


    @RequestMapping("spuList")
    public List<SpuInfo>  getSpuList( SpuInfo spuInfo){
        List<SpuInfo> spuInfoList = manageService.getSpuInfoList(spuInfo);
        return  spuInfoList;
    }


    //http://localhost:8082/baseSaleAttrList
    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr>  baseSaleAttrList(){
        List<BaseSaleAttr> baseSaleAttrList = manageService.getBaseSaleAttrList();
        return  baseSaleAttrList;
    }

    //保存spu信息
    @RequestMapping("saveSpuInfo")
    public  void  saveSpuInfo(@RequestBody SpuInfo spuInfo){
        manageService.saveSpuInfo(spuInfo);
    }









    //回显
}
