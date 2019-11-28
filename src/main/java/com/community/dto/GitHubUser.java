package com.community.dto;

import lombok.Data;

@Data
public class GitHubUser {
    private  String login;
    private  Long id;
    private String bio;
    private String avatar_url;
}
