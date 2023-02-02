package com.example.anigo;

public interface FeedUserDbContract {

    void Create(String login, String password, String token);
    boolean Delete();
    FeedUserLocal CheckIfExist();
    FeedUserLocal GetUser(String login, String password);
    String GetToken(String login, String password);
    boolean IsExist();

}
