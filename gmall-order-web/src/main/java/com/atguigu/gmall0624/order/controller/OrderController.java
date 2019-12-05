package com.atguigu.gmall0624.order.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0624.bean.UserAddress;
import com.atguigu.gmall0624.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@Controller
public class OrderController {

     //@Autowired
    @Reference
    UserInfoService  userInfoService;


     @RequestMapping("trade")
     @ResponseBody //返回json字符chuan
     public List<UserAddress> trade(String userId){
        return   userInfoService.findUserAddressByUserId(userId);
      }

}


