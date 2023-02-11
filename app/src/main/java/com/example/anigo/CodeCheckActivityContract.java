package com.example.anigo;

interface  CodeCheckActivityContract {
    interface View{
        void OnSuccess(String message);
        void OnError(String err_message);
    }
    interface Presenter{
        void CheckCode(String code, String entered_code);
    }
}
