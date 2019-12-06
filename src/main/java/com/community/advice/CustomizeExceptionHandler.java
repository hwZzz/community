package com.community.advice;

import com.community.dto.ResultDTO;
import com.community.exception.CustomizeErrorCode;
import com.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    Object handle(Throwable e, Model model, HttpServletRequest request){

        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            //返回JSON
            if(e instanceof CustomizeException){
                return ResultDTO.errorOf((CustomizeException) e);
            }else{
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
        }else {
            //错误页面跳转

            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else{
                model.addAttribute("message","服务冒烟了，请稍后再试！！！");
            }
            return new ModelAndView("error");
        }
    }

}
