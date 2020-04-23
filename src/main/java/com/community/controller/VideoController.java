package com.community.controller;

import com.community.cache.HotTagCache;
import com.community.dto.PageDTO;
import com.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class VideoController {

    @GetMapping("/videos")
    public String index(){
        return "videos";
    }

    @GetMapping("/video")
    public String video(@RequestParam(name = "aid") String aid,
                        @RequestParam(name = "page",defaultValue = "1") String page,
                        Model model){
        model.addAttribute("aid",aid);
        model.addAttribute("page",page);
        return "video";
    }
}
