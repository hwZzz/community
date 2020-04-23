package com.community.service;

import com.community.mapper.AdMapper;
import com.community.model.Ad;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdService {
    @Autowired
    private AdMapper adMapper;

    public List<Ad> list(String pos){
        List<Ad> list = adMapper.list(System.currentTimeMillis(), pos);
        return list;
    }
}
