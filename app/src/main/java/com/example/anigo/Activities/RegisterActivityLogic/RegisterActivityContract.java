package com.example.anigo.Activities.RegisterActivityLogic;

public interface RegisterActivityContract {
    interface View {
        void onSuccess(String message);
        void onError(String message, String body);
        void onError(String message);
    }
    interface Presenter{
        void Register(String password, String login, String Email, int RoleId);
    }
}
