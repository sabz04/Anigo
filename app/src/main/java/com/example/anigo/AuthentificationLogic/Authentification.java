package com.example.anigo.AuthentificationLogic;

import android.content.Context;
import android.util.Log;

import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.Models.UserResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.example.anigo.Activities.MainActivityLogic.UserLoginAuthClass;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Authentification implements AuthentificationInterface.Process{


    private AuthentificationInterface.Listener listener;
    private FeedUserDbHelper db_helper;
    private OkHttpClient client;
    private FeedUserLocal user_local;
    private UserResponse user_response;

    private String login = "";
    private String password = "";

    public Authentification(AuthentificationInterface.Listener listener, Context context){
            this.listener=listener;
            this.db_helper = new FeedUserDbHelper(context);
            this.client = new OkHttpClient();
    }

    @Override
    public void Auth() {
        user_local = db_helper.CheckIfExist();
        if(user_local == null || user_local.Login == null || user_local.Password == null){
            listener.AuthError("Пользователь не авторизован!");
            return;
        }
        else{
            this.login = user_local.Login;
            this.password = user_local.Password;
        }
        UserLoginAuthClass auth_user = new UserLoginAuthClass(login, password);
        String json = new Gson().toJson(auth_user);
        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(RequestOptions.request_url_login)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.AuthError(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    String json_body = response.body().string();
                    user_response = new Gson().fromJson(json_body, UserResponse.class);
                    listener.AuthSuccess(user_response.token);
                    listener.AuthSuccess(user_response.token, user_response.user.id);
                }
                else {
                    listener.AuthError(response.message());
                }
            }
        });
    }
    @Override
    public void Auth(String login, String password) {

    }
}
