package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
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

public class CommentLikePresenter implements AuthentificationInterface.Listener, ViewHolderContract.ILikeAdd {

    Context context;
    Authentification authentification;
    OkHttpClient okHttpClient;
    ViewHolderContract.View view;

    int commentId = -1;

    public CommentLikePresenter( ViewHolderContract.View view,Context context) {
        this.context = context;
        this.view = view;
        okHttpClient = new OkHttpClient();
        authentification = new Authentification(this, this.context);
    }
    @Override
    public void AddLike(int commentId) {
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

        CommentLikeAddClass likeAddObject = new CommentLikeAddClass(user_id, commentId);
        String json = new Gson().toJson(likeAddObject);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request = new Request.Builder()

                .url(RequestOptions.request_url_add_like)
                .post(formBody)
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
                    view.OnSuccessAddLike(json_body);
                }
                else {
                    view.OnErrorAddLike(json_body);
                }
            }
        });
    }


}
