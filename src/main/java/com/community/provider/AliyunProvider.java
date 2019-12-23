package com.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class AliyunProvider {
    @Value("${aliyun.file.accesskeyid}")
    private String accessKeyId;

    @Value("${aliyun.file.accesskeysecret}")
    private String accessKeySecret;

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = "http://oss-cn-chengdu.aliyuncs.com";

    // 创建OSSClient实例。
    OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

    public String upload(){

        // 上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("<yourlocalFile>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ossClient.putObject("<yourBucketName>", "<yourObjectName>", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
