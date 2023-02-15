package com.example.anigo.Activities.PasswordChangeActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.RequestsHelper.RequestOptions;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordChangePresenter implements PasswordChangeActivityContract.Presenter, AuthentificationInterface.Listener{

    private String password;

    FeedUserDbHelper db_helper;

    FeedUserLocal user_local;

    PasswordChangeActivityContract.View view;

    Authentification authentification;

    OkHttpClient client;

    Context context;

    public PasswordChangePresenter(PasswordChangeActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.client = new OkHttpClient();
        this.db_helper = new FeedUserDbHelper(context);
        this.user_local = db_helper.CheckIfExist();

    }

    @Override
    public void ChangePass(String password, String email) {

        String end = String.format(RequestOptions.request_url_change_pass, email, password);
        Request request = new Request.Builder()

                .url(end)
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

                    String resp_status = json_body;

                    if(user_local != null){
                        db_helper.Delete();
                        db_helper.Create(user_local.Login, password, "token");
                        view.OnSuccess(resp_status);
                    }
                    else{
                        view.OnSuccessNoLogon(json_body);
                    }



                }
                else {
                    view.OnError("error " + json_body);
                }

            }
        });
    }

    @Override
    public void AuthSuccess(String message) {


    }

    @Override
    public void AuthError(String message) {

    }

    @Override
    public void AuthSuccess(String token, int user_id) {

    }
}
