package com.example.anigo.InnerDatabaseLogic;

import com.example.anigo.Models.User;

public class FeedUserLocal {
    public int Id;
    public String Login;
    public String Password;
    public String Token;

    public FeedUserLocal(int id, String login, String password, String token) {
        Id = id;
        Login = login;
        Password = password;
        Token = token;
    }
    public FeedUserLocal() {

    }
}
