package com.community.mapper;

import com.community.dto.CommentDTO;
import com.community.enums.CommentTypeEnum;
import com.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment (parent_id,type,commentator,gmt_create,gmt_modified,like_count,content) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id = #{parentId}")
    Comment getById(@Param("parentId") Long parentId);

    @Select("select * from comment where id = #{parentId} and type = 1")
    List<Comment> listByQuestionId(@Param("parentId") Long parentId);
}
