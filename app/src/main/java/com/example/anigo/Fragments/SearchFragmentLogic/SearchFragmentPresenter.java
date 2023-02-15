package com.example.anigo.Fragments.SearchFragmentLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.DateDeserializer;
import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.Models.AnimeResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragmentPresenter implements  SearchFragmentContract.Presenter, AuthentificationInterface.Listener{

    private SearchFragmentContract.View view;
    private Authentification authentification;
    private FeedUserDbHelper db_helper;
    private OkHttpClient client;
    private Context context;
    private FeedUserLocal user_local;

    private String login = "";
    private String password = "";
    private String search = "";

    int page = -1;

    public SearchFragmentPresenter(SearchFragmentContract.View view, Context context){
        this.view = view;
        this.db_helper = new FeedUserDbHelper(context);
        this.client = new OkHttpClient();
        this.context = context;
    }

    @Override
    public void Search(String search) {

    }

    @Override
    public void Search(String search, int page, Context context) {
        authentification =new Authentification(this, this.context);
        this.search = search;
        this.page = page;
        authentification.Auth();
    }

    @Override
    public void AuthSuccess(String token) {
        user_local = db_helper.CheckIfExist();
        if(user_local == null){
            view.onError("Пользователь не авторизован!");
            return;
        }
        else{
            this.login = user_local.Login;
            this.password = user_local.Password;
        }
        Request request = new Request.Builder()
                .url(String.format(RequestOptions.request_url_animes_get,page, search))
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    String response_body = response.body().string();
                    AnimeResponse response_animes = new Gson().fromJson(response_body, AnimeResponse.class);
                    view.onSuccess("Поиск успешен.", response_animes.animes, response_animes.currentPage, response_animes.pages);
                }
                else {
                   view.onError(response.message());
                }
            }
        });


    }

    @Override
    public void AuthError(String message) {
        view.onError(message);
    }

    @Override
    public void AuthSuccess(String token, int user_id) {

    }
}
