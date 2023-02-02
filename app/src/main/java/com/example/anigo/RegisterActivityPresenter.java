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

public class RegisterActivityPresenter implements RegisterActivityContract.Presenter{

    RegisterActivityContract.View view;

    FeedUserDbHelper db_helper;

    OkHttpClient client;

    String login = "";

    String password = "";

    Gson gson;


    public RegisterActivityPresenter(RegisterActivityContract.View view){
        this.view = view;
        gson = new Gson();
        client = new OkHttpClient();
    }
    @Override
    public void Register(String password, String login, String Email, int RoleId) {

        UserLoginRegisterClass reg_user = new UserLoginRegisterClass(login, password, Email, RoleId);
        //converting to json
        String json = new Gson().toJson(reg_user);

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


                    UserLoginAuthClass user = gson.fromJson(json_body, UserLoginAuthClass.class);
                     view.onSuccess(json_body);

                }
                else {
                    view.onError("error " + json_body);
                }

            }
        });
    }

}
