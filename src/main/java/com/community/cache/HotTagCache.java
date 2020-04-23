package com.community.cache;

import com.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();

    public void updateTags(Map<String, Integer> tags){
        int max = 10;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        tags.forEach((name, priority) -> {
           HotTagDTO hotTagDTO = new HotTagDTO();
           hotTagDTO.setName(name);
           hotTagDTO.setPriority(priority);
           if (priorityQueue.size()<max){
               priorityQueue.add(hotTagDTO);
           }else{
               //与队列头部比较判断是否要插入
               HotTagDTO minHot = priorityQueue.peek();
               if (hotTagDTO.compareTo(minHot)>0){
                   priorityQueue.poll();
                   priorityQueue.add(hotTagDTO);
               }
           }
        });

        List<String> sortedTags = new ArrayList<>();

        HotTagDTO poll = priorityQueue.poll();
        while(poll != null){
            sortedTags.add(0,poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
        System.out.println(hots);
    }
}
