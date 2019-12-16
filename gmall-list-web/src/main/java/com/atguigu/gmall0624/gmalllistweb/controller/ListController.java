package com.atguigu.gmall0624.gmalllistweb.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0624.bean.*;
import com.atguigu.gmall0624.service.ListService;
import com.atguigu.gmall0624.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class ListController {

    @Reference
    ListService listService;

    @Reference
    ManageService manageService;

    @RequestMapping("list.html")
    //@ResponseBody
    public String getList(SkuLsParams skuLsParams, HttpServletRequest request){
        //设置每页显示的数据
        skuLsParams.setPageSize(2);
        SkuLsResult search = listService.search(skuLsParams);
        List<SkuLsInfo> skuLsInfoList = search.getSkuLsInfoList();
        request.setAttribute("skuLsInfoList",skuLsInfoList);
        //获取平台属性值得Id集合
        List<String> attrValueIdList = search.getAttrValueIdList();


        //如何保存用户的查询条件
        String urlParam=makeUrlParam(skuLsParams);
        System.out.println(urlParam);

        List<BaseAttrInfo>   baseAttrInfoList=  manageService.getAttrListByIds(attrValueIdList);
        //点击平台属性值，平台属性消失
        //使用itco迭代器
        //声明一个面包屑集合
        ArrayList<BaseAttrValue> baseAttrValueArrayList = new ArrayList<BaseAttrValue>();
        if(baseAttrInfoList!=null&&baseAttrInfoList.size()>0) {
            for (Iterator<BaseAttrInfo> iterator = baseAttrInfoList.iterator(); iterator.hasNext(); ) {
                BaseAttrInfo baseAttrInfo = iterator.next();
                //value
                //得到属性值得
                List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

                for (BaseAttrValue baseAttrValue : attrValueList) {
                    if (skuLsParams.getValueId() != null && skuLsParams.getValueId().length > 0) {
                        //从url中取得valueId
                        for (String valueId : skuLsParams.getValueId()) {
                            if(valueId.equals(baseAttrValue.getId())) {
                                iterator.remove();
                                BaseAttrValue baseAttrValueSelected = new BaseAttrValue();
                                baseAttrValueSelected.setValueName(baseAttrInfo.getAttrName()+":"+baseAttrValue.getValueName());

                                // 去除重复数据
                                String makeUrlParam = makeUrlParam(skuLsParams, valueId);
                                baseAttrValueSelected.setUrlParam(makeUrlParam);
                                baseAttrValueArrayList.add(baseAttrValueSelected);
                            }
                        }
                    }
                }



            }
        }
        //设置分页
        request.setAttribute("totalPages",search.getTotalPages());
        request.setAttribute("pageNo",skuLsParams.getPageNo());
        //面包屑显示
        request.setAttribute("baseAttrValueArrayList",baseAttrValueArrayList);
        request.setAttribute("baseAttrInfoList",baseAttrInfoList);
        request.setAttribute("keyword",skuLsParams.getKeyword());
        request.setAttribute("urlParam",urlParam);
        return "list";
    }
    //制作参数

    /**
     *
     * @param skuLsParams   表示用户url中输入的查询条件参数
     * @param excludeValueIds  表示用户点击面包屑时传递的属性值Id
     * @return
     */
    private String makeUrlParam(SkuLsParams skuLsParams,    String ...excludeValueIds) {
        String  urlParam="";
        //用户走的是全文检索
        if(skuLsParams.getKeyword()!=null&&skuLsParams.getKeyword().length()>0){
            urlParam+= "keyword="+skuLsParams.getKeyword();
            //判断是否有平台属性值

        }
        //用户走的是三级分类Id
        if(skuLsParams.getCatalog3Id()!=null&&skuLsParams.getCatalog3Id().length()>0){

            urlParam+="catalog3Id="+skuLsParams.getCatalog3Id();

        }
        if(skuLsParams.getValueId()!=null&&skuLsParams.getValueId().length>0){
            //循环
            for (String valueId : skuLsParams.getValueId()) {
                //用户点击时平台属性值Id
                if (excludeValueIds!=null && excludeValueIds.length>0){
                    String excludeValueId = excludeValueIds[0];
                    if (excludeValueId.equals(valueId)){
                        // 跳出代码，后面的参数则不会继续追加【后续代码不会执行】
// 不能写break；如果写了break；其他条件则无法拼接！
                        continue;
                    }
                }

                urlParam+="&valueId="+valueId;
            }
        }
        return urlParam;
    }
}
