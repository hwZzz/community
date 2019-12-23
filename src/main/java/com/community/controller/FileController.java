package com.community.controller;

import com.community.dto.FileDTO;
import com.community.provider.AliyunProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileController {

    @PostMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request, MultipartFile file) throws Exception{
        if (file == null || file.getSize() <= 0) {
            throw new Exception("头像不能为空");
        }
        String  nameHz= file.getOriginalFilename(); //上传的文件名 + 后缀    如  asd.png
        String type = "";
        if(nameHz.contains(".png") || nameHz.contains(".jpg")){
            type="/img";
        }
        if(nameHz.contains(".mp4") || nameHz.contains(".ogv")){
            type="/video";
        }else {
            type="/file";
        }
        AliyunProvider ossClient = new AliyunProvider();
        String keyName = ossClient.uploadImg2Oss(file,type);
        String imgUrl = ossClient.getImgUrl(keyName);

//         FileDTO fileDTO = new FileDTO();
//         fileDTO.setSuccess(1);
//         fileDTO.setUrl("/images/test.jpg");
//         return fileDTO;
    }
}
