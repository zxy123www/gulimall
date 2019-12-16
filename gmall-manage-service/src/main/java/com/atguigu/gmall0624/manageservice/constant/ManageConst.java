package com.atguigu.gmall0624.manageservice.constant;

public class ManageConst {
    public static final String SKUKEY_PREFIX="sku:";

    public static final String SKUKEY_SUFFIX=":info";

    public static final int SKUKEY_TIMEOUT=24*60*60;
    //锁的过期时间
    public static final int SKULOCK_EXPIRE_PX=10000;
  //所得Key后缀
    public static final String SKULOCK_SUFFIX=":lock";
}
