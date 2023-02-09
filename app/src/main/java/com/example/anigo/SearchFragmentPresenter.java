package com.example.anigo;

import android.content.Context;

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

    SearchFragmentContract.View view;

    Authentification authentification;

    FeedUserDbHelper db_helper;

    OkHttpClient client;

    String login = "";

    String password = "";

    String search = "";
    Context context;

    int page = -1;

    public SearchFragmentPresenter(SearchFragmentContract.View view, Context context){
        this.view = view;
        this.db_helper = new FeedUserDbHelper(context);
        client = new OkHttpClient();
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
        FeedUserLocal user_local = new FeedUserLocal();
        if(user_local == null){
            view.onError("user null");
            return;
        }
        else{
            this.login = user_local.Login;
            this.password = user_local.Password;
        }

        FeedUserLocal user = db_helper.CheckIfExist();

        Request request = new Request.Builder()
                .url(String.format(RequestOptions.request_url_animes_get,page, search))
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = client.newCall(request);

        /*Log.v("headers", )*/

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    String response_body = response.body().string();

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.registerTypeAdapter(Date.class, new DateDeserializer()).create();
                    AnimeResponse response_animes = gson.fromJson(response_body, AnimeResponse.class);

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
