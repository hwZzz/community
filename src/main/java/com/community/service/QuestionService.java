package com.community.service;

import com.community.dto.PageDTO;
import com.community.dto.QuestionDTO;
import com.community.exception.CustomizeErrorCode;
import com.community.exception.CustomizeException;
import com.community.mapper.QuestionMapper;
import com.community.mapper.UserMapper;
import com.community.model.Question;
import com.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO list(Integer page, Integer size) {  //将两个类拼装在一起。

        PageDTO pageDTO = new PageDTO();

        Integer totalPage;

        Integer totalCount = questionMapper.count();      //用户提问的总数

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

        List<Question> questions = questionMapper.list(offset,size);  //问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();     //准备放入问题列表+用户的组合类的列表

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator()); //通过ID找到User
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将question的属性赋值给questionDTO
            questionDTO.setUser(user); //将USER赋给questionDTO
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOList);
        return pageDTO;
    }

    public PageDTO list(Integer userId, Integer page, Integer size) {

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

        pageDTO.setQuestions(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
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
            questionMapper.create(question);
        }else{
            //跟新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }

    public void incView(Integer id) {
        Question question = questionMapper.getById(id) ;
        questionMapper.updateViewCount(id);

    }
}
