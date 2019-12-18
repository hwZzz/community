package com.community.mapper;

import com.community.model.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {

    void insert(Notification notification);
}
