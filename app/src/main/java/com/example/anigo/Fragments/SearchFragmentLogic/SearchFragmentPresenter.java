package com.example.anigo.Fragments.SearchFragmentLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.DateDeserializer;
import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.Models.AnimeResponse;
import com.example.anigo.Models.FilterObject;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    private FilterObject filterObject;

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
    public void Search(FilterObject filterObject, Context context) {
        authentification =new Authentification(this, this.context);
        this.filterObject = filterObject;
        authentification.Auth();
    }

    @Override
    public void AuthSuccess(String token) {



    }

    @Override
    public void AuthError(String message) {
        view.onError(message);
    }

    @Override
    public void AuthSuccess(String token, int user_id) {
        String json = new Gson().toJson(filterObject);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(String.format(RequestOptions.request_url_animes_get))
                .post(formBody)
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
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    AnimeResponse animeResponse = new Gson().fromJson(json_body, AnimeResponse.class);
                    view.onSuccess("ok",animeResponse.animes, animeResponse.currentPage, animeResponse.pages);
                }
                else {
                    view.onError(json_body);
                }

            }
        });
    }
}
