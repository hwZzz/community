package com.community.model;

import lombok.Data;

@Data
public class Ad {
    private Long id;
    private String title;
    private String url;
    private String image;
    private Long gmtStart;
    private Long gmtEnd;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer status;
    private String pos;
}
