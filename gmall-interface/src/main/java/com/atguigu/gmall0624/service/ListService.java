package com.atguigu.gmall0624.service;


import com.atguigu.gmall0624.bean.SkuLsInfo;
import com.atguigu.gmall0624.bean.SkuLsParams;
import com.atguigu.gmall0624.bean.SkuLsResult;

public interface ListService {

    /**
     * 保存商品信息到es中
     * @param skuLsInfo
     */
    public void saveSkuInfo(SkuLsInfo skuLsInfo);

    /**
     * 全文检索商品
     * @param skuLsParams
     * @return
     */

    public SkuLsResult search(SkuLsParams skuLsParams);

    /**
     * 更新热度排名
     * @param skuId
     */
    public void incrHotScore(String skuId);
}
