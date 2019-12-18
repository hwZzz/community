package com.community.service;

import com.community.dto.NotificationDTO;
import com.community.dto.PageDTO;
import com.community.enums.NotificationStatusEnum;
import com.community.enums.NotificationTypeEnum;
import com.community.exception.CustomizeErrorCode;
import com.community.exception.CustomizeException;
import com.community.mapper.NotificationMapper;
import com.community.mapper.UserMapper;
import com.community.model.Notification;
import com.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;


    public PageDTO list(Long userId, Integer page, Integer size) {
        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();

        Integer totalPage;

        Integer totalCount = notificationMapper.countByUserId(userId);      //用户提问的总数

        if (totalCount % size ==0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if(page<1){
            page =1;     //容错，小于1则为1
        }

        if(page>totalPage){
            page = totalPage; //大于最大页数，则为最后一页
        }

        pageDTO.setPagination(totalPage,page);

        Integer offset = size *(page -1); //分页,offset（从多少开始)
        List<Notification> notifications = notificationMapper.listByReceiver(userId,offset,size);  //问题列表

        if(notifications.size() == 0){
            return pageDTO;
        }


        List<NotificationDTO> notificationDTOS = new ArrayList<>();     //准备放入问题列表+用户的组合类的列表
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        pageDTO.setData(notificationDTOS);
        return pageDTO;
    }

    public Long unreadCount(Long userId) {
        Long unreadCount = notificationMapper.unreadCount(userId,NotificationStatusEnum.UNREAD.getStatus());
        return unreadCount;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.getById(id);
            if(notification == null){
                throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
            }
            if(!Objects.equals(notification.getReceiver(), user.getId())){
                throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
            }

            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            notificationMapper.updateById(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
