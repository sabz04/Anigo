package com.example.anigo.Activities.AnimeActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.CommentsActivityLogic.CommentsActivityContract;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.AnimeCommentAddClass;
import com.example.anigo.Models.FavouriteAddClass;
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

public class AnimeActivityPresenterAddComment implements AnimeActivityContract.PresenterAddComment, AuthentificationInterface.Listener {

    private Context _context;
    private Authentification _authentification;
    private CommentsActivityContract.View _view;
    private OkHttpClient _okHttpClient;

    private int _animeId;
    private String _animeComment;


    public AnimeActivityPresenterAddComment(Context context, CommentsActivityContract.View view) {
        this._context = context;
        this._view = view;
        this._okHttpClient = new OkHttpClient();
    }

    @Override
    public void AddComment(String comment, int animeId) {
        _authentification = new Authentification(this, this._context);
        _authentification.Auth();
        this._animeComment = comment;
        this._animeId = animeId;
    }

    @Override
    public void AuthSuccess(String message) {


    }

    @Override
    public void AuthError(String message) {

    }

    @Override
    public void AuthSuccess(String token, int user_id) {

        AnimeCommentAddClass commentAddClass = new AnimeCommentAddClass(
                user_id,
                this._animeId,
                this._animeComment
        );
        String json = new Gson().toJson(commentAddClass);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request = new Request.Builder()

                .url(RequestOptions.request_url_add_comment)
                .post(formBody)
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = _okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                _view.OnErrorAddComment(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    _view.OnSuccessAddComment(json_body);
                }
                else {
                    _view.OnErrorAddComment(json_body);
                }

            }
        });
    }
}
