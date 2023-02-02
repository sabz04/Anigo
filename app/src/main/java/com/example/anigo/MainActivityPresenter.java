package com.example.anigo;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    MainActivityContract.View view;

    String password = "";
    String login = "";

    public MainActivityPresenter(MainActivityContract.View view){
        this.view = view;
    }

    @Override
    public void  Login(String pass, String log, Context context) {


        FeedUserDbHelper db = new FeedUserDbHelper(context);

        OkHttpClient client = new OkHttpClient();

        FeedUserLocal user = db.CheckIfExist();

        if(user != null){
            this.login = user.Login;
            this.password = user.Password;
        }
        else {
            this.login = log;
            this.password = pass;
        }

        UserLoginAuthClass auth_user = new UserLoginAuthClass(password, login);

        //converting to json
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
                view.onError("Ошибка запроса." + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    view.onSuccess("Вход успешен.");

                    db.Create(login, password, response.body().string());
                    Log.d("user created", "hehe, yes! he is actually added to inner database");


                }
                else {
                    view.onError("Такой пользователь не найден.", response.body().string());

                }
            }
        });





    }
}
