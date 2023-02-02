package com.example.anigo;

public interface AuthentificationInterface {
    interface Process{
        void Auth();
        void Auth(String login, String password);
    }
    interface Listener{
        void AuthSuccess(String message);
        void AuthError(String message);
    }

}
