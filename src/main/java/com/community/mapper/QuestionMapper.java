package com.community.mapper;

import com.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface QuestionMapper {

    void create(Question qeustion);

    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    Integer count();

    List<Question> listByUserId(@Param("userId") Long userId,@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    Integer countByUserId(@Param("userId") Long userId);

    Question getById(@Param("id") Long id);

    void update(Question question);

    void updateViewCount(@Param("id") Long id);

    void updateCommentCount(@Param("id") Long id);

    List<Question> selectRelated(Question question);
}
