package com.example.anigo;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnimeActivityPresenter implements AnimeActivityContract.Presenter, AuthentificationInterface.Listener {

    AnimeActivityContract.View view;

    Authentification authentification;

    FeedUserDbHelper db_helper;

    OkHttpClient client;

    String login = "";

    String password = "";

    Gson gson;

    int id =-1;

    Context context;

    public AnimeActivityPresenter(AnimeActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
        client = new OkHttpClient();
        gson= new Gson();
    }

    @Override
    public void GetAnime(int id) {

        authentification = new Authentification(this, context);


        this.id =id;
        authentification.Auth();
    }


    @Override
    public void AuthSuccess(String token) {


        Request request = new Request.Builder()

                .url(RequestOptions.request_url_anime_get + id)
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


                    Anime anime = gson.fromJson(json_body, Anime.class);
                    System.out.printf("2");
                    view.OnSuccess(anime);

                }
                else {
                    view.OnError("error " + json_body);
                }

            }
        });
    }

    @Override
    public void AuthError(String message) {

    }
}
