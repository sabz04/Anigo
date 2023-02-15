package com.example.anigo.Fragments.FragmentLikedLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.FavResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentLikedPresenter implements FragmentLikedContract.Presenter , AuthentificationInterface.Listener {

    FragmentLikedContract.View view;

    Authentification authentification;

    OkHttpClient client;

    Gson gson;

    Context context;

    int page = -1;

    public FragmentLikedPresenter(FragmentLikedContract.View view, Context context) {
        this.view = view;
        this.context = context;
        client = new OkHttpClient();
        gson= new Gson();
    }


    @Override
    public void GetFavs(int page) {
        this.page = page;
        authentification = new Authentification(this, this.context);
        authentification.Auth();
    }
    @Override
    public void AuthSuccess(String token) {

    }

    @Override
    public void AuthError(String message) {

    }

    @Override
    public void AuthSuccess(String token, int user_id) {
        Request request = new Request.Builder()

                .url(String.format(RequestOptions.request_url_get_favs,this.page, user_id))
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

                    FavResponse fav_response = gson.fromJson(json_body, FavResponse.class);
                    System.out.printf("2");
                    view.OnSuccess(fav_response.favs, fav_response.currentPage, fav_response.pages);

                }
                else {
                    view.OnError("error " + json_body);
                }

            }
        });
    }


}
