package com.example.anigo;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivityPresenter implements RegisterActivityContract.Presenter, OkHttpContract.Presenter{
    RegisterActivityContract.View view;

    OkHttpUserHelper presenter;

    public RegisterActivityPresenter(RegisterActivityContract.View view){
        this.view = view;
    }
    @Override
    public void Register(String password, String login, String Email, int RoleId) {


        presenter = new OkHttpUserHelper(this);

        presenter.SendPostRegister(login, password, Email, RoleId);
    }

    @Override
    public void OnSuccess(String message) {
        view.onSuccess(message);
    }

    @Override
    public void OnSuccess(String message, Anime[] animes, int current_page, int pages_count) {

    }

    @Override
    public void OnError(String message) {
        view.onSuccess(message);
    }

    @Override
    public void OnSuccess(Anime anime) {

    }
}
