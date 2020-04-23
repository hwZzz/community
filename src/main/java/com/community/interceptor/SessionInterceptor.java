package com.community.interceptor;

import com.community.enums.AdPosEum;
import com.community.mapper.UserMapper;
import com.community.model.User;
import com.community.service.AdService;
import com.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdService adService;

    @Autowired
    private NotificationService notificationService;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置context级别的属性
        request.getServletContext().setAttribute("redirectUri",redirectUri);
        //没有登录也可以查看导航
//        request.getServletContext().setAttribute("ads",adService.list());

        for(AdPosEum adPos : AdPosEum.values()){
            request.getServletContext().setAttribute(adPos.name(),adService.list(adPos.name()));
        }

        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length!=0)
            for(Cookie cookie : cookies){
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if(user != null){
                        HttpSession session = request.getSession();
                        request.getSession().setAttribute("user",user);
                        Integer unreadCount = notificationService.unreadCount(user.getId());
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;
                }
            }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
