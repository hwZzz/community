package com.community.mapper;

import com.community.model.Ad;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdMapper {
    List<Ad> list(@Param("currentTime")Long currentTime,@Param("pos") String pos);
}
