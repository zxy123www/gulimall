package com.atguigu.gmall0624.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0624.bean.*;
import com.atguigu.gmall0624.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ManageController {

    @Reference
    ManageService  manageService;


    //查询所有的一级分类
    @RequestMapping("getCatalog1")
    public List<BaseCatalog1>   getCatalog1(){
        List<BaseCatalog1> catalogList1 = manageService.getCatalog1();
        return  catalogList1;

    }
    //根据一级分类Id查询二级分类
    @RequestMapping("getCatalog2")
    public List<BaseCatalog2>   getCatalog2(String catalog1Id){
        List<BaseCatalog2> catalogList2 = manageService.getCatalog2(catalog1Id);
        return  catalogList2;

    }
    //根据二级分类Id查询三级分类

    @RequestMapping("getCatalog3")
    public List<BaseCatalog3>   getCatalog3(String catalog2Id){
        List<BaseCatalog3> catalogList3 = manageService.getCatalog3(catalog2Id);
        return  catalogList3;

    }

    //根据3级分类的Id查询商品的平台属性attrInfoList
   @RequestMapping("attrInfoList")
    public List<BaseAttrInfo>   attrInfoList(String catalog3Id){
       List<BaseAttrInfo> attrList = manageService.getattrInfoList(catalog3Id);
       return   attrList;

   }
      //添加平台属性
    //提交的数据为Json数据
    @RequestMapping("saveAttrInfo")

    public void  saveAttrInfo(@RequestBody  BaseAttrInfo  baseAttrInfo){
        manageService.saveAttrInfo(baseAttrInfo);
    }

    //修改之间根据attrId查询商品平台属性
    @RequestMapping("getAttrValueList")
    public List<BaseAttrValue>  getAttrValueList(String attrId ){

      BaseAttrInfo baseAttrInfo= manageService.getAttrInfo(attrId);

      return   baseAttrInfo.getAttrValueList();
    }



}
