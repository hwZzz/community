package com.community.controller;

import com.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    @PostMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(){
         FileDTO fileDTO = new FileDTO();
         fileDTO.setSuccess(1);
         fileDTO.setUrl("/images/test.jpg");
         return fileDTO;
    }
}
