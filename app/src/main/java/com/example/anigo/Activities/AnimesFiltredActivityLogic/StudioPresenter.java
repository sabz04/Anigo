package com.example.anigo.Activities.AnimesFiltredActivityLogic;

import android.content.Context;

import com.example.anigo.Activities.AnimeActivityLogic.AnimeActivityContract;
import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.Anime;
import com.example.anigo.Models.AnimeResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudioPresenter implements AnimeFiltredContract.PresenterStudio, AuthentificationInterface.Listener {

    private Context context;
    private Authentification authentification;
    private AnimeFiltredContract.View view;
    private OkHttpClient okHttpClient;

    private String studioName;
    private int currentPage;

    public StudioPresenter(Context context, AnimeFiltredContract.View view) {
        this.context = context;
        this.view = view;
        okHttpClient = new OkHttpClient();
        authentification = new Authentification(this, context);
    }

    @Override
    public void GetAnimes(int page, String studioName) {
        this.currentPage = page;
        this.studioName = studioName;
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

                .url(String.format(RequestOptions.request_url_get_animes_by_studio, currentPage, studioName))
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.ErrorGetAnimesByStudio(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    AnimeResponse animeResponse = new Gson().fromJson(json_body, AnimeResponse.class);
                    view.SuccessGetAnimesByStudio(animeResponse.animes);
                    //_view.OnSuccessGetComments(commentResponse.pages, commentResponse.currentPage, commentResponse.currentPageItemCount, commentResponse.comments,user_id);

                }
                else {
                    //_view.OnErrorGetComments(json_body);
                    view.ErrorGetAnimesByStudio(json_body);
                }

            }
        });
    }
}
