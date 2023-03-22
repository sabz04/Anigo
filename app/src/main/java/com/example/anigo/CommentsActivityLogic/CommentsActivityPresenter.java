package com.example.anigo.CommentsActivityLogic;

import android.content.Context;

import com.example.anigo.Activities.MainActivityLogic.MainActivityContract;
import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.AnimeCommentResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentsActivityPresenter implements CommentsActivityContract.Presenter, AuthentificationInterface.Listener {

    CommentsActivityContract.View view;
    OkHttpClient client;
    Context context;
    Authentification authentification;

    int page;
    int animeId;

    public CommentsActivityPresenter(CommentsActivityContract.View view, Context context) {
        this.context = context;
        this.view = view;
        client = new OkHttpClient();
    }

    @Override
    public void GetComments(int currentPage, int animeId) {
        this.page = currentPage;
        this.animeId = animeId;
        authentification = new Authentification(this, context);
        authentification.Auth();
    }

    @Override
    public void AuthSuccess(String message) {

    }

    @Override
    public void AuthError(String message) {

    }

    @Override
    public void AuthSuccess(String token, int user_id) {
        Request request = new Request.Builder()

                .url(String.format(RequestOptions.request_url_get_comments, animeId,page))
                .get()
                .addHeader("Authorization", "Bearer " + token )
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
                    AnimeCommentResponse commentResponse = new Gson().fromJson(json_body, AnimeCommentResponse.class);
                    view.OnSuccessGetComments(commentResponse,user_id);
                }
                else {
                    view.OnError(json_body);
                }

            }
        });
    }

    }

