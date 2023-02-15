package com.example.anigo.Fragments.HomeFragmentLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.AnimeResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragmentPresenter implements HomeFragmentContract.Presenter, AuthentificationInterface.Listener {


    private OkHttpClient client;
    private Context context;
    private int page =-1;
    private HomeFragmentContract.View view;
    private Authentification authentification;

    public HomeFragmentPresenter(HomeFragmentContract.View view, Context context) {
        this.context = context;
        this.view = view;
        this.client = new OkHttpClient();
    }

    @Override
    public void GetFavs(int page) {
        this.page = page;
        authentification = new Authentification(this, this.context);
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
                .url(String.format(RequestOptions.request_url_get_popular,this.page))
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
                    AnimeResponse animeResponse = new Gson().fromJson(json_body, AnimeResponse.class);
                    view.OnSuccess(animeResponse.animes, animeResponse.currentPage, animeResponse.pages);
                }
                else {
                    view.OnError(json_body);
                }

            }
        });
    }
}
