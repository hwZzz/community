package com.community.service;

import com.community.dto.CommentDTO;
import com.community.enums.CommentTypeEnum;
import com.community.enums.NotificationStatusEnum;
import com.community.enums.NotificationTypeEnum;
import com.community.exception.CustomizeErrorCode;
import com.community.exception.CustomizeException;
import com.community.mapper.CommentMapper;
import com.community.mapper.NotificationMapper;
import com.community.mapper.QuestionMapper;
import com.community.mapper.UserMapper;
import com.community.model.Comment;
import com.community.model.Notification;
import com.community.model.Question;
import com.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;


    public void insert(Comment comment, User commentator) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.getById(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            //回复问题
            Question question = questionMapper.getById(dbComment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);

            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentMapper.incCommentCount(parentComment);

            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());

         }else{
                //回复问题
                Question question = questionMapper.getById(comment.getParentId());
                if(question == null){
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
                comment.setCommentCount(0);
                commentMapper.insert(comment);
                question.setCommentCount(1);
                questionMapper.updateCommentCount(question.getId());

                //创建通知
                createNotify(comment,question.getCreator(), commentator.getName(), question.getTitle(),NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    /**
     *
     * @param comment
     * @param receiver
     * @param notifierName 评论或点赞的人的名字
     * @param outerTitle    问题标题
     * @param notificationType 已读还是未读
     * @param outerId
     */
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId){

        if(receiver == comment.getCommentator()){
            return ;
        }

        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);

    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        List<Comment> comments = commentMapper.listByQuestionId(id,type.getType());

        if(comments.size() == 0){
            return new ArrayList<>();
        }
        //使用lamda 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //转换成userid

        //获取评论人并转换成map
        List<User> users = userMapper.getUsers(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为commentDTO
       List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
