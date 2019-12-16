package com.atguigu.gmall0624.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0624.bean.SkuAttrValue;
import com.atguigu.gmall0624.bean.SkuInfo;
import com.atguigu.gmall0624.bean.SkuSaleAttrValue;
import com.atguigu.gmall0624.bean.SpuSaleAttr;
import com.atguigu.gmall0624.service.ListService;
import com.atguigu.gmall0624.service.ManageService;
import com.sun.deploy.net.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    ManageService manageService;


    @Reference
    ListService listService;
    //根据商品id查找商品信息，图片信息并在页面显示
    @RequestMapping("{skuId}.html")
    public String skuInfoPage(@PathVariable String skuId, HttpServletRequest request , Model model ){
        //查询商品的信息通过SkuId/&&查询商品信息关联的图片集合
        SkuInfo skuInfo = manageService.selectSkuInfoBySkuId(skuId);
        request.setAttribute("skuInfo",skuInfo);
        //*** 销售属性，销售属性值回显并锁定！
        List<SpuSaleAttr> spuSaleAttrList=manageService.getSpuSaleAttrBySkuInfo(skuInfo);
        request.setAttribute("spuSaleAttrList",spuSaleAttrList);
        //根据skuId更改页面有版本和颜色决定
        List<SkuSaleAttrValue> skuSaleAttrValueList= manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        //把列表变换成 valueid1|valueid2|valueid3 ：skuId  的 哈希表 用于在页面中定位查询
        String valueIdsKey="";

        Map<String, String> valuesSkuMap= new HashMap<>();

        for (int i = 0; i < skuSaleAttrValueList.size(); i++) {
            SkuSaleAttrValue  skuSaleAttrValue= skuSaleAttrValueList.get(i);
            if(valueIdsKey.length()!=0){
                valueIdsKey= valueIdsKey+"|";
            }
            valueIdsKey=valueIdsKey+skuSaleAttrValue.getSaleAttrValueId();

            if((i+1)== skuSaleAttrValueList.size()||!skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueList.get(i+1).getSkuId())  ){

                valuesSkuMap.put(valueIdsKey,skuSaleAttrValue.getSkuId());
                valueIdsKey="";
            }
        }

        //把map变成json串
        String valuesSkuJson = JSON.toJSONString(valuesSkuMap);

        model.addAttribute("valuesSkuJson",valuesSkuJson);

        listService.incrHotScore(skuId);
        return "item";
    }
}
