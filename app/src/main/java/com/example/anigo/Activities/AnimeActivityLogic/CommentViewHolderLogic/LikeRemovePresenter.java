package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LikeRemovePresenter implements ViewHolderContract.ILikeRemove, AuthentificationInterface.Listener {

    Context context;
    Authentification authentification;
    OkHttpClient okHttpClient;
    ViewHolderContract.View view;

    int commentId = -1;

    public LikeRemovePresenter(ViewHolderContract.View view, Context context) {
        this.context = context;
        this.view = view;
        authentification = new Authentification(this, context);
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void RemoveLike(int commentId) {
        this.commentId = commentId;
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
                .url(String.format(RequestOptions.request_url_like_delete, commentId, user_id))
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnErrorAddLike(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    AnimeComment comment = new Gson().fromJson(json_body, AnimeComment.class);
                    view.OnSuccessRemoveLike(comment);
                }
                else {
                    view.OnErrorRemoveLike(json_body);
                }
            }
        });
    }
}
