package com.example.anigo.Activities.AnimeActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.CommentsActivityLogic.CommentsActivityContract;
import com.example.anigo.Models.Anime;
import com.example.anigo.Models.AnimeCommentResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnimeActivityGetFranchizeAnimesPresenter implements AuthentificationInterface.Listener, AnimeActivityContract.PresenterGetLinkedAnimes {
    private Context context;
    private Authentification authentification;
    private AnimeActivityContract.View view;
    private OkHttpClient okHttpClient;

    private String frName;

    public AnimeActivityGetFranchizeAnimesPresenter(Context context, AnimeActivityContract.View view) {
        this.context = context;
        this.view = view;
        okHttpClient = new OkHttpClient();
        authentification = new Authentification(this,context);
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

                .url(RequestOptions.request_url_get_animes_by_franchize+frName)
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnErrorGetLinkedAnimes(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    Anime[] animes = new Gson().fromJson(json_body, Anime[].class);
                    view.OnSuccessGetLinkedAnimes(animes);
                    //_view.OnSuccessGetComments(commentResponse.pages, commentResponse.currentPage, commentResponse.currentPageItemCount, commentResponse.comments,user_id);

                }
                else {
                    //_view.OnErrorGetComments(json_body);
                    view.OnErrorGetLinkedAnimes(json_body);
                }

            }
        });
    }

    @Override
    public void GetAnimes(String frName) {
        this.frName = frName;
        authentification.Auth();
    }
}
