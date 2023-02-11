package com.example.anigo;

interface PasswordChangeActivityContract {
    interface View{
        void OnSuccess(String message);
        void OnError(String message);
        void OnSuccessNoLogon(String message);
    }
    interface Presenter{
        void ChangePass(String password, String email);
    }
}
