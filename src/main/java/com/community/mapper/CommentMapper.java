package com.community.mapper;

import com.community.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface CommentMapper {
    void insert(Comment comment);

    Comment getById(@Param("parentId") Long parentId);

    List<Comment> listByQuestionId(@Param("parentId") Long parentId,@Param("type1")Integer type1);
}
