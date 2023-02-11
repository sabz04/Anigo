package com.example.anigo;

public interface CodeSendActivityContract {
    interface View{
        void OnSuccess(String code);
        void OnError(String error_msg);
    }
    interface Presenter{
        void GetCode(String email);
    }
}
