package com.atguigu.gmall0624.gmallusermanage.controller;

import com.atguigu.gmall0624.bean.UserInfo;
import com.atguigu.gmall0624.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @RequestMapping("findAll")
    public List<UserInfo> findAll(){
        List<UserInfo> infoList = userInfoService.findAll();
        return infoList;
    }
}
