package com.community.controller;

import com.community.model.User;
import com.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DeleteController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/delquestion/{id}")
    public String delquestion(@PathVariable(name = "id") Long id, HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");
        Long creatorId = questionService.findByCreatorId(id);

        if(creatorId.equals(user.getId())){
            questionService.delete(id);
            return "redirect:/";
        }
            return "redirect:/";
    }
}
