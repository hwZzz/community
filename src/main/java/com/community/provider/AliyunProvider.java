package com.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

@Service
public class AliyunProvider {
    @Value("${aliyun.file.accesskeyid}")
    private String accessKeyId;

    @Value("${aliyun.file.accesskeysecret}")
    private String accessKeySecret;

    //空间
    @Value("${aliyun.file.bucket-name}")
    private String bucketName;

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    @Value("${aliyun.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.file.expires}")
    private Long expires;

    Log log = LogFactory.getLog(AliyunProvider.class);

    //文件存储目录    (上传时在key前面加上目录,如果云上没有，则自动创建)
    private String date = "";

    private OSS  ossClient;


    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }
//
//    public String upload(InputStream inputStream,String fileName){
//        String generatedFileName;
//        String[] filePaths = fileName.split("\\.");
//        if(filePaths.length>1){
//            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length-1];
//        }else{
//            return  null;
//        }
//
//        // 上传文件流。
//        try {
//            inputStream = new FileInputStream("<yourlocalFile>");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        ossClient.putObject("hwzzz", generatedFileName, inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        return "";
//    }
//
//    /**
//     * 上传图片 直接获取本地资源路径
//     *
//     * @param url
//     * @throws Exception
//     */
//    public void uploadImg2Oss(String url) throws Exception {
//        File fileOnServer = new File(url);
//        FileInputStream fin;
//        try {
//            fin = new FileInputStream(fileOnServer);
//            String[] split = url.split("/");
//            this.uploadFile2OSS(fin, split[split.length - 1]);
//        } catch (FileNotFoundException e) {
//            throw new Exception("图片上传失败");
//        }
//    }


    /**
     * 上传图片
     * @param file
     * @return    key   可根据key获取上到到服务器的志愿和删除等操作
     * @throws Exception
     */
    public String uploadImg2Oss(MultipartFile file, String typeDate) throws Exception {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        this.date=typeDate;
//    if (file.getSize() > 1024 * 1024) {
//      throw new Exception("上传图片大小不能超过1M！");
//    }

        String originalFilename = file.getOriginalFilename();  //获取文件名
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase(); //获取后缀名
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;   //随机生成文件名
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, date + fileName, instream);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.date + split[split.length - 1]);
        }
        return null;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase("jpeg") ||
                FilenameExtension.equalsIgnoreCase("jpg") ||
                FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase("pptx") ||
                FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase("docx") ||
                FilenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + expires);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
//
//    /**
//     * 删除单个文件
//     */
//    public void delFile(String key){
//        ossClient.deleteObject(bucketName, key);
//    }

}
