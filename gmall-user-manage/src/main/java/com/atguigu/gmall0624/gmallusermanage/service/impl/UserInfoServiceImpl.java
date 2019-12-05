package com.atguigu.gmall0624.gmallusermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0624.bean.UserAddress;
import com.atguigu.gmall0624.bean.UserInfo;
import com.atguigu.gmall0624.gmallusermanage.user.mapper.UserAddressMapper;
import com.atguigu.gmall0624.gmallusermanage.user.mapper.UserInfoMapper;
import com.atguigu.gmall0624.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserAddressMapper userAddressMapper;
    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserInfo> findUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public List<UserInfo> findByNickName(String nickName) {
        return null;
    }

    @Override
    public void addUser(UserInfo userInfo) {

    }

    @Override
    public void updUser(UserInfo userInfo) {

    }

    @Override
    public void delUser(UserInfo userInfo) {

    }

    @Override
    public List<UserAddress> findUserAddressByUserId(String userId) {
        UserAddress userAddress=new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    public List<UserAddress> findUserAddressByUserId(UserAddress userAddress) {
        return userAddressMapper.select(userAddress);
    }
}
