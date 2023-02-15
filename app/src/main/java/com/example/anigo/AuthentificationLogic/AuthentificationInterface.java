package com.example.anigo.AuthentificationLogic;

public interface AuthentificationInterface {
    interface Process{
        void Auth();
        void Auth(String login, String password);
    }
    interface Listener{
        void AuthSuccess(String message);
        void AuthError(String message);
        void AuthSuccess(String token, int user_id);
    }

}
