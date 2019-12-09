package com.atguigu.gmall0624.manageservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0624.bean.*;
import com.atguigu.gmall0624.manageservice.mapper.*;
import com.atguigu.gmall0624.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    BaseCatalog1Mapper  baseCatalog1Mapper;

    @Autowired
    BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    BaseCatalog3Mapper baseCatalog3Mapper;

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    SpuInfoMapper  spuInfoMapper;


     @Autowired
     BaseSaleAttrMapper baseSaleAttrMapper;

     @Autowired
     SpuSaleAttrMapper spuSaleAttrMapper;

     @Autowired
     SpuImageMapper spuImageMapper;

     @Autowired
     SpuSaleAttrValueMapper spuSaleAttrValueMapper;

     @Autowired
     SkuInfoMapper skuInfoMapper;

     @Autowired
     SkuImageMapper skuImageMapper;

     @Autowired
     SkuAttrValueMapper  skuAttrValueMapper;

     @Autowired
     SkuSaleAttrValueMapper  skuSaleAttrValueMapper;

    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        //通用Mapper查询
        BaseCatalog2 baseCatalog2=new    BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        List<BaseCatalog2> baseCatalog2List = baseCatalog2Mapper.select(baseCatalog2);
        return baseCatalog2List;
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
       BaseCatalog3 baseCatalog3=new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        List<BaseCatalog3> catalog3List = baseCatalog3Mapper.select(baseCatalog3);
        return catalog3List;
    }

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
        //根据3级fenleiId查询平台属性
        BaseAttrInfo baseAttrInfo=new  BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.select(baseAttrInfo);
        return baseAttrInfoList;
    }

    @Override
    @Transactional
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //添加分为插入两张表 baseAttrInfo ，BaseAttrValue
        //1.添加 或者修改 baseAttrInfo
        if(baseAttrInfo.getId()==null){
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }else{
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        }


        BaseAttrValue baseAttrValueDel = new BaseAttrValue();
        baseAttrValueDel.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValueDel);
        System.out.println("删除数据");
         //2.添加 BaseAttrValue
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if(attrValueList!=null&& attrValueList.size()!=0){

            for (int i = 0; i < attrValueList.size(); i++) {
                BaseAttrValue  baseAttrValue=attrValueList.get(i);
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(baseAttrValue);
            }

        }

    }

    //查询平台属性的值
    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {

        //创建属性对象
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        //创建属性值得对象
        BaseAttrValue  baseAttrValue=new BaseAttrValue();
        //根据attrId字段查询对象
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        List<BaseAttrValue> attrValueList = baseAttrValueMapper.select(baseAttrValue);
        //给属性对象返回
        baseAttrInfo.setAttrValueList(attrValueList);
        return baseAttrInfo;
    }

    @Override
    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
            return baseSaleAttrMapper.selectAll();
    }




    //大保存
    //需要保存多张表
    //      SpuInfo
    //		SpuImage
    //		SpuSaleAttr
    //		SpuSaleAttrValue
    @Override
    @Transactional
    public void saveSpuInfo(SpuInfo spuInfo) {
         //1添加SupInfo表
        if (spuInfo.getId()==null || spuInfo.getId().length()==0) {
            spuInfo.setId(null);
            int i = spuInfoMapper.insertSelective(spuInfo);
        }else{
            spuInfoMapper.updateByPrimaryKeySelective(spuInfo);
        }
        //2添加图片表,先执行删除再执行添加避免重复添加

        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);

        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);

        // 销售属性值 删除，插入
        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuInfo.getId());
        spuSaleAttrValueMapper.delete(spuSaleAttrValue);
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(spuImageList!=null&&spuImageList.size()!=0){
            for (int m= 0; m < spuImageList.size(); m++) {
                SpuImage  spuImage1=spuImageList.get(m);
                spuImage1.setId(null);
                spuImage1.setSpuId(spuInfo.getId());
                spuImageMapper.insertSelective(spuImage1);
            }
        }
        //添加属性名称表先执行删除再执行添加避免重复添加
        List<SpuSaleAttr> spuSaleAttrList=spuInfo.getSpuSaleAttrList();
        if(spuSaleAttrList!=null&&spuSaleAttrList.size()!=0){
            for (int n = 0; n < spuSaleAttrList.size(); n++) {
                SpuSaleAttr   spuSaleAttr1=spuSaleAttrList.get(n);
                spuSaleAttr1.setId(null);
                spuSaleAttr1.setSpuId(spuInfo.getId());
                //添加属性值表先执行删除再执行添加避免重复添加
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr1.getSpuSaleAttrValueList();
                if(spuSaleAttrValueList!=null&&spuSaleAttrValueList.size()!=0){
                    for (int k = 0; k < spuSaleAttrValueList.size(); k++) {
                        SpuSaleAttrValue spuSaleAttrValue1 = spuSaleAttrValueList.get(k);
                        spuSaleAttrValue1.setId(null);
                        spuSaleAttrValue1.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insert( spuSaleAttrValue1);
                    }
                }
                spuSaleAttrMapper.insertSelective(spuSaleAttr1);


            }
        }





    }

    @Override
    public List<SpuImage> getSpuImageList(SpuImage spuImage) {
        return  spuImageMapper.select(spuImage);
    }

    @Override
    public List<BaseAttrInfo> getattrInfoList(String catalog3Id) {

        List<BaseAttrInfo> baseAttrInfoList=   baseAttrInfoMapper.getattrInfoList(catalog3Id);
        return  baseAttrInfoList;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrList(spuId);
    }
    //大保存
    //sku_info，sku_attr_value，sku_sale_attr_value，sku_image
    @Override
    @Transactional
    public void saveSkuInfo(SkuInfo skuInfo) {
      //保存 sku_info
      if(skuInfo.getId()==null )  {
          skuInfoMapper.insertSelective(skuInfo);
      }
      //添加sku_image
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
       if(skuImageList.size()!=0){
           for (int i = 0; i < skuImageList.size(); i++) {
               SkuImage skuImage= skuImageList.get(i);
               skuImage.setSkuId(skuInfo.getId());
               skuImageMapper.insertSelective(skuImage);
           }
       }
       //添加sku_attr_value
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(skuAttrValueList!=null&&skuAttrValueList.size()!=0){
            for (int k = 0; k < skuAttrValueList.size(); k++) {
                SkuAttrValue skuAttrValue=skuAttrValueList.get(k);
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(skuAttrValue);
            }
        }
        //添加sku_sale_attr_value
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(skuSaleAttrValueList!=null&&skuSaleAttrValueList.size()!=0){
            for (int m= 0; m < skuSaleAttrValueList.size(); m++) {
                SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueList.get(m);
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);

            }
        }
    }


}
