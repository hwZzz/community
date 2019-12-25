package com.community.service;

import com.community.dto.PageDTO;
import com.community.dto.QuestionDTO;
import com.community.dto.QuestionQueryDTO;
import com.community.exception.CustomizeErrorCode;
import com.community.exception.CustomizeException;
import com.community.mapper.QuestionMapper;
import com.community.mapper.UserMapper;
import com.community.model.Question;
import com.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO list(String search,Integer page, Integer size) {  //将两个类拼装在一起。


        if(StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search," ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PageDTO pageDTO = new PageDTO();

        Integer totalPage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        Integer totalCount = questionMapper.countBySearch(questionQueryDTO);      //用户提问的总数

        if (totalCount % size ==0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if(page<1){
            page =1;     //容错，小于1则为1
        }

        if(page>totalPage){
            page = totalPage; //大于最大页数，则为最后一页
        }
        pageDTO.setPagination(totalPage,page);

        Integer offset = size *(page -1); //分页,offset（从多少开始)

        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionMapper.selectBySearch(questionQueryDTO);  //问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();     //准备放入问题列表+用户的组合类的列表

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator()); //通过ID找到User
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将question的属性赋值给questionDTO
            questionDTO.setUser(user); //将USER赋给questionDTO
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);
        return pageDTO;
    }

    public PageDTO list(Long userId, Integer page, Integer size) {

        PageDTO pageDTO = new PageDTO();

        Integer totalPage;

        Integer totalCount = questionMapper.countByUserId(userId);      //用户提问的总数

        if (totalCount % size ==0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if(page<1){
            page =1;     //容错，小于1则为1
        }

        if(page>totalPage){
            page = totalPage; //大于最大页数，则为最后一页
        }

        pageDTO.setPagination(totalPage,page);



        Integer offset = size *(page -1); //分页,offset（从多少开始)


        List<Question> questions = questionMapper.listByUserId(userId,offset,size);  //问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();     //准备放入问题列表+用户的组合类的列表

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator()); //通过ID找到User
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将question的属性赋值给questionDTO
            questionDTO.setUser(user); //将USER赋给questionDTO
            questionDTOList.add(questionDTO);
        }

        pageDTO.setData(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.getById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);//将question的属性赋值给questionDTO
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.create(question);
        }else{
            //跟新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }

    public void incView(Long id) {
        Question question = questionMapper.getById(id) ;
        questionMapper.updateViewCount(id);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
     if(StringUtils.isBlank(queryDTO.getTag())){
         return new ArrayList<>();
     }
     String[] tags = StringUtils.split(queryDTO.getTag(),",");
     String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
     Question question = new Question();
     question.setId(queryDTO.getId());
     question.setTag(regexpTag);

     List<Question> questions = questionMapper.selectRelated(question);
     List<QuestionDTO> questionDTOS = questions.stream().map(q ->{
         QuestionDTO questionDTO = new QuestionDTO();
         BeanUtils.copyProperties(q,questionDTO);
         return questionDTO;
     }).collect(Collectors.toList());
     return questionDTOS;
    }
}
