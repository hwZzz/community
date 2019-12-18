package com.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不要换个试试?"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行恢复?"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在了，要不要换一个试试"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空"),
    NOTIFICATION_NOT_FOUND(2008,"消息莫非是不翼而飞了？"),
    READ_NOTIFICATION_FAIL(2009,"兄弟你这是读别人的信息呢？");


    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message){
        this.message = message;
        this.code = code;
    }
}
