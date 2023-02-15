package com.example.anigo.Activities.RegisterActivityLogic;

import com.example.anigo.Models.User;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivityPresenter implements RegisterActivityContract.Presenter{

    RegisterActivityContract.View view;

    OkHttpClient client;

    String login = "";

    String password = "";

    String json = "";

    UserLoginRegisterClass reg_user;
    public RegisterActivityPresenter(RegisterActivityContract.View view){
        this.view = view;
        client = new OkHttpClient();
    }
    @Override
    public void Register(String password, String login, String Email, int RoleId) {

        this.login = login;
        this.password = password;

        reg_user = new UserLoginRegisterClass(login, password, Email, RoleId);
        //converting to json
        json = new Gson().toJson(reg_user);
        //SABIROVVV!_
        // generating new request with okhttp, sure that thats no need to declare that variables outside the method
        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(RequestOptions.request_url_register)
                .post(formBody)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200){
                    User user = new Gson().fromJson(json_body, User.class);
                    if(user != null)
                        view.onSuccess(json_body);
                    else{
                        view.onError("Ошибка запроса. Повторите попытку.");
                    }
                }
                else {
                    view.onError(json_body);
                }

            }
        });
    }

}
