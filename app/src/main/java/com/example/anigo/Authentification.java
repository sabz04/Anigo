package com.example.anigo;

import android.content.Context;
import android.util.Log;

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


    AuthentificationInterface.Listener listener;

    FeedUserDbHelper db_helper;

    OkHttpClient client;

    String login = "";

    String password = "";

    Gson gson;

    public Authentification(AuthentificationInterface.Listener listener, Context context){
            this.listener=listener;
            this.db_helper = new FeedUserDbHelper(context);
            this.client = new OkHttpClient();
            gson= new Gson();
    }

    @Override
    public void Auth() {

        FeedUserLocal user_local = db_helper.CheckIfExist();
        if(user_local == null || user_local.Login == null || user_local.Password == null){
            listener.AuthError("user null");
            return;
        }
        else{
            this.login = user_local.Login;
            this.password = user_local.Password;
        }

        UserLoginAuthClass auth_user = new UserLoginAuthClass(login, password);
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
                listener.AuthError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){



                    String json_body = response.body().string();

                    UserResponse user_response = gson.fromJson(json_body, UserResponse.class);


                    FeedUserLocal userLocal = db_helper.CheckIfExist();
                    if(userLocal == null){
                        db_helper.Create(user_response.user.name, user_response.user.password, user_response.token);

                        Log.d("LOCAL_DATABASE", "hehe, yes! he is actually added to inner database");
                    }
                    else {
                        Log.d("LOCAL_DATABASE ", "USER is already here");
                    }
                    //db_helper.Delete();
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
