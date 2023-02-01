package com.example.anigo;

public interface FeedUserDbContract {

    void Create(String login, String password, String token);
    void Delete(String login, String password);
    FeedUserLocal CheckIfExist();
    FeedUserLocal GetUser(String login, String password);
    String GetToken(String login, String password);
    boolean IsExist();

}
