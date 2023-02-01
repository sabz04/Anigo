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

    public RegisterActivityPresenter(RegisterActivityContract.View view){
        this.view = view;
    }
    @Override
    public void Register(String password, String login, String Email, int RoleId) {
        OkHttpClient client = new OkHttpClient();

        UserLoginRegisterClass reg_user = new UserLoginRegisterClass(login, password, Email, RoleId );

        //converting to json
        String json = new Gson().toJson(reg_user);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"),json);
        Request request = new Request.Builder()

                .url(RequestOptions.request_url_register)
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
                if(response.code() == 201)
                    view.onSuccess("Новый пользователь создан.");
                else {
                    view.onError(response.body().string());
                }
            }
        });


    }
}
