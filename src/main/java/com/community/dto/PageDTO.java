package com.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO<T> {
    private List<T> data;
    private boolean showPrevious;    //上一页
    private boolean showFirstPage;  //第一页
    private boolean showNext;  //下一页
    private boolean showEndPage; //最后一页

    private Integer page;  //当前页
    private List<Integer> pages = new ArrayList<>();//当前页码数左右的页码
    private Integer totalPage;

    public void setPagination(Integer totalPage, Integer page) {  //计算总页数
        this.totalPage = totalPage;
        this.page = page;

        pages.add(page);
        for(int i=1;i<=3;i++){
            if(page-i>0){
                pages.add(0,page-i);
            }

            if(page +i<=totalPage){
                pages.add(page+i);
            }
        }


        if(page == 1){  //当是第一页时没有上一页按钮
            showPrevious = false;
        }else {
            showPrevious = true;
        }

        if(page == totalPage){  //当是最后一页时没有下一页按钮
            showNext = false;
        }else {
            showNext = true;
        }

        //是否展示第一页按钮
        if(pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }

        //是否展示最后一页按钮
        if(pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }
}
