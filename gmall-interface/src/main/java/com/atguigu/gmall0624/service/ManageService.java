package com.atguigu.gmall0624.service;

import com.atguigu.gmall0624.bean.*;

import java.util.List;

public interface ManageService {

    public List<BaseCatalog1> getCatalog1();

    public List<BaseCatalog2> getCatalog2(String catalog1Id);

    public List<BaseCatalog3> getCatalog3(String catalog2Id);

    public List<BaseAttrInfo> getAttrList(String catalog3Id);

    public void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    public BaseAttrInfo getAttrInfo(String attrId);

    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    public List<BaseSaleAttr>  getBaseSaleAttrList();

    void saveSpuInfo(SpuInfo spuInfo);

    List<SpuImage> getSpuImageList(SpuImage spuImage);


    List<BaseAttrInfo> getattrInfoList(String catalog3Id);

    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);

    void saveSkuInfo(SkuInfo skuInfo);

    SkuInfo selectSkuInfoBySkuId(String skuId);



    List<SpuSaleAttr> getSpuSaleAttrBySkuInfo(SkuInfo skuInfo);

    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);

    List<BaseAttrInfo> getAttrListByIds(List<String> attrValueIdList);
}
