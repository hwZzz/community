package com.community.mapper;

import com.community.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {

    void insert(Notification notification);

    Long unreadCount(@Param("userId") Long userId,@Param("status") int status);

    Integer countByUserId(@Param("userId") Long userId);

    List<Notification> listByReceiver(@Param("userId") Long userId,@Param("offset") Integer offset,@Param("size") Integer size);

    Notification getById(Long id);

    void updateById(Notification notification);
}
