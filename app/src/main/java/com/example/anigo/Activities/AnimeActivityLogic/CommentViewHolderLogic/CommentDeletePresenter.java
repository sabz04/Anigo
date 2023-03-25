package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.CommentLikeAddClass;
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

public class CommentDeletePresenter implements AuthentificationInterface.Listener, ViewHolderContract.ICommentDelete {
    OkHttpClient client;
    Context context;
    Authentification authentification;
    ViewHolderContract.View view;

    public CommentDeletePresenter(Context context, ViewHolderContract.View view) {
        this.context = context;
        this.view = view;
        this.client = new OkHttpClient();
        authentification = new Authentification(this, context);
    }

    int commentId;

    @Override
    public void AuthSuccess(String message) {

    }

    @Override
    public void AuthError(String message) {

    }

    @Override
    public void AuthSuccess(String token, int user_id) {

        Request request = new Request.Builder()
                .url(RequestOptions.request_url_remove_comment + commentId)
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnErrorAddLike(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){

                   view.OnSuccessRemoveComment(json_body);
                }
                else {
                    view.OnErrorRemoveComment(json_body);
                }
            }
        });
    }

    @Override
    public void RemoveComment(int commentId) {
        this.commentId = commentId;
        authentification.Auth();
    }
}
