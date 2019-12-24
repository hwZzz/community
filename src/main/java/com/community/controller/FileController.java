package com.community.controller;

import com.community.dto.FileDTO;
import com.community.provider.AliyunProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileController {

    @Autowired
    AliyunProvider aliyunProvider;

    @PostMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws Exception{

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");

        if (file == null || file.getSize() <= 0) {
            throw new Exception("头像不能为空");
        }
        String  nameHz= file.getOriginalFilename(); //上传的文件名 + 后缀    如  asd.png
        String type = "";   //确定存到哪个目录
        if(nameHz.contains(".png") || nameHz.contains(".jpg")){
            type ="img/";
        }
        if(nameHz.contains(".mp4") || nameHz.contains(".ogv")){
            type="video/";
        }
        if(StringUtils.isBlank(type)){
            type="file/";
        }
        String keyName = aliyunProvider.uploadImg2Oss(file,type);
        String imgUrl = aliyunProvider.getImgUrl(keyName);

             FileDTO fileDTO = new FileDTO();
             fileDTO.setSuccess(1);
             fileDTO.setUrl(imgUrl);
             return fileDTO;
    }
}
