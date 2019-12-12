package com.community.mapper;

import com.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    void insert(User user);

    User findByToken(@Param("token") String token);

    User findById(@Param("id") Long id);

    User findByAccountId(@Param("accountId")String accountId);

    void update(User User);

    List<User> getUsers(List<Long> userIds);
}
