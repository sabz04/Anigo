package com.example.anigo.Activities.CodeSendActivityLogic;

import android.content.Context;

import com.example.anigo.RequestsHelper.RequestOptions;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CodeSendActivityPresenter implements  CodeSendActivityContract.Presenter{

    CodeSendActivityContract.View view;

    Context context;

    OkHttpClient client;

    String email;

    public CodeSendActivityPresenter(CodeSendActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.client = new OkHttpClient();
    }

    @Override
    public void GetCode(String email) {

        this.email = email;
        Request request = new Request.Builder()

                .url(RequestOptions.request_url_get_code + this.email)
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){

                    String code = json_body;
                    view.OnSuccess(code);

                }
                else {
                    view.OnError(json_body);
                }

            }
        });
    }


}
