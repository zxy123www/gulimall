package com.atguigu.gmall0624.manage;


import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
 public class GmallManageWebApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Test
    public void textFileUpload() throws IOException, MyException {
        //表示读取配置文件tracker.conf
        String file = this.getClass().getResource("/tracker.conf").getFile();
        //初始化
        ClientGlobal.init(file);
        //创建TrackerClient对象
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer=trackerClient.getConnection();
        StorageClient storageClient=new StorageClient(trackerServer,null);
        //获取本地文件的地址
        String orginalFilename="d://img//hjb.jpg";
        String[] upload_file = storageClient.upload_file(orginalFilename, "jpg", null);
        for (int i = 0; i < upload_file.length; i++) {
            String s = upload_file[i];
            System.out.println("s = " + s);
        }
    }

}
