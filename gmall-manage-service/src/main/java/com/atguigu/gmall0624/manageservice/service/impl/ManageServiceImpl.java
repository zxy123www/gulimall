package com.atguigu.gmall0624.manageservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0624.bean.*;
import com.atguigu.gmall0624.config.RedistUtil;
import com.atguigu.gmall0624.manageservice.constant.ManageConst;
import com.atguigu.gmall0624.manageservice.mapper.*;
import com.atguigu.gmall0624.service.ManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

     @Autowired
    RedistUtil  redistUtil;
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

    @Override
    public SkuInfo selectSkuInfoBySkuId(String skuId) {
        return getSkuInfoRedisSet(skuId);

    }

    private SkuInfo getSkuInfoRedisSet(String skuId) {
        //set k2 v1 px 10000 nx k2是锁 px 10000 代表执行时间
        SkuInfo skuInfo = new SkuInfo();
        Jedis jedis =null;
        try {
             jedis = redistUtil.getJedis();

            //商品数据如何存储在缓存的？
            //使用redis那种数据类型存储 String  List  Hash Set Zset
            //String存储一个字符串
            //list存放队列
            //set用来去重的 交集，差集不急
            //Hash存储对象
            //定义一个Key   保证key的唯一性
            // user:userId:info
            // String skuKey =sku:skuId:info
            String skuKey = ManageConst.SKUKEY_PREFIX + skuId + ManageConst.SKUKEY_SUFFIX;

                //key存在
                String skuJson = jedis.get(skuKey);
               if(skuJson==null){
                   System.out.println("缓存中没有数据");
                   //缓存中没有数据
                   //查询数据库并且上锁

                   //定义一个锁的Key
                   String skuLockKey=ManageConst.SKUKEY_SUFFIX+skuId+ManageConst.SKULOCK_SUFFIX;
                   //锁的值
                   String token= UUID.randomUUID().toString().replaceAll("-","");
                   //执行锁
                   String lockKey = jedis.set(skuLockKey, token, "NX", "PX", ManageConst.SKULOCK_EXPIRE_PX);

                   if(lockKey.equals("OK")){
                       System.out.println("获取分布式的锁");
                       //获取分布式的锁
                       //查询数据库
                   skuInfo= getSkuInfoDb(skuId);
                   jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT, JSON.toJSONString(skuInfo));

                     //解锁
                       String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                      //执行删锁
                       jedis.eval(script, Collections.singletonList(skuLockKey), Collections.singletonList(token));
                   return  skuInfo;
                   }else{
                       //等待
                       Thread.sleep(100);

                       //调用
                       return  selectSkuInfoBySkuId(skuId);
                   }

               }else{
                   //缓存中有数据
                   skuInfo=JSON.parseObject(skuJson,SkuInfo.class);
                   return skuInfo;
               }



        }catch(Exception E){

        }finally {
            if(jedis!=null){
                //关闭
                jedis.close();
            }
        }

        //测试工具类
        /*try {
            Jedis jedis = redistUtil.getJedis();
            jedis.set("test","text_value" );
        }catch (JedisConnectionException e){
            e.printStackTrace();
        }*/

        return  getSkuInfoDb(skuId);
    }

    //获取数据库中的数据
    private SkuInfo getSkuInfoDb(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        //查询图片集合
        SkuImage skuImage=new  SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        //封装到skuInfo
        skuInfo.setSkuImageList(skuImageList);

        //获取平台属性值得Id集合

        //SkuAttrValue
        SkuAttrValue skuAttrValue=new  SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);
        skuInfo.setSkuAttrValueList(skuAttrValueList);
        return skuInfo;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrBySkuInfo(SkuInfo skuInfo) {

        List<SpuSaleAttr>   spuSaleAttrList=   spuSaleAttrMapper.getSpuSaleAttrListBySkuInfo(skuInfo.getId(),skuInfo.getSpuId());
        return spuSaleAttrList;
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        List<SkuSaleAttrValue> skuSaleAttrValueList=    skuSaleAttrValueMapper.getSkuSaleAttrValueListBySpu(spuId);
        return skuSaleAttrValueList;
    }

    @Override
    public List<BaseAttrInfo> getAttrListByIds(List<String> attrValueIdList) {
        //将集合转换成字符串
        String attrValueIds  = StringUtils.join(attrValueIdList.toArray(), ",");

        System.out.println(attrValueIds); // 171,81,120,167,82,83 ...
        return baseAttrInfoMapper.selectAttrInfoListByIds(attrValueIds);
    }


} 
