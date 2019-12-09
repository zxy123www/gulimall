package com.atguigu.gmall0624.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
public class FileUploadController {

    @Value("${fileServer.url}")
    private String    fileUrl;

    @RequestMapping("fileUpload")
    public String fileUpload(MultipartFile file) throws IOException, MyException {

        String imgUrl=fileUrl;

        if (file != null) {

            //表示读取配置文件tracker.conf
            String conffile = this.getClass().getResource("/tracker.conf").getFile();
            //初始化
            ClientGlobal.init(conffile);
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            //获取本地文件的地址
            //获取上传的文件名称zly.jpg
            String orginalFilename = file.getOriginalFilename();
            //获取文件后缀名
            String extName = StringUtils.substringAfterLast(orginalFilename, ".");
            String[] upload_file = storageClient.upload_file(file.getBytes(), extName, null);
            for (int i = 0; i < upload_file.length; i++) {
                String path= upload_file[i];
                imgUrl+="/"+path;
            }

        }
        return imgUrl;
    }
}


