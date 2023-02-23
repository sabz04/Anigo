package com.example.anigo.Activities.AnimeActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.Anime;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnimeActivityPresenterGetComments implements AuthentificationInterface.Listener, AnimeActivityContract.PresenterGetComments {

    private AnimeActivityContract.View _view;
    private Authentification _authentification;
    private OkHttpClient _okHttpClient;
    private Context _context;

    private int _animeId;

    public AnimeActivityPresenterGetComments(Context context, AnimeActivityContract.View view) {
        this._view = view;
        this._context = context;
        this._okHttpClient = new OkHttpClient();
    }
    @Override
    public void GetComments(int animeId) {
        _animeId = animeId;
        _authentification = new Authentification(this,this._context);
        _authentification.Auth();
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

                .url(String.format(RequestOptions.request_url_get_comments, _animeId))
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = _okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                _view.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    AnimeComment[] animeComments = new Gson().fromJson(json_body, AnimeComment[].class);
                    _view.OnSuccessGetComments(animeComments);

                }
                else {
                    _view.OnErrorGetComments(json_body);
                }

            }
        });
    }


}
