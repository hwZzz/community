package com.community.controller;

import com.community.dto.CommentDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CommentController {

    @PostMapping("/comment")
    public Object post(@RequestBody CommentDTO commentDTO){

    }
}
