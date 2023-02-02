package com.example.anigo;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class OkHttpUserHelper implements OkHttpContract.OkHttpRequest{

    public OkHttpContract.Presenter presenter;

    private OkHttpClient client;

    private  FeedUserDbHelper db_helper;

    private Gson gson;

    Authentification auth;

    private Context context;

    public  OkHttpUserHelper(OkHttpContract.Presenter presenter, Context context){

        this.presenter = presenter;
        this.context = context;
        this.gson = new Gson();
        this.db_helper = new FeedUserDbHelper(context);
        this.context = context;
        FeedUserLocal user = db_helper.CheckIfExist();
        if(user != null)
            this.client = new OkHttpClient();
        else
            this.client = new OkHttpClient();
    }
    public OkHttpUserHelper(OkHttpContract.Presenter presenter){
        this.presenter = presenter;
        this.gson = new Gson();
        this.db_helper = new FeedUserDbHelper(context);
        context = MyApp.getContext();
        FeedUserLocal user = db_helper.CheckIfExist();
        this.client = new OkHttpClient();
    }

    @Override
    public void SendPostLogin(String login, String password) {

        UserLoginAuthClass auth_user = new UserLoginAuthClass(login, password);
        //converting to json
        String json = new Gson().toJson(auth_user);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request = new Request.Builder()

                .url(RequestOptions.request_url_login)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presenter.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){

                    String json_body = response.body().string();
                    FeedUserLocal userLocal = db_helper.CheckIfExist();
                    if(userLocal == null){
                        db_helper.Create(login, password, json_body);
                        Log.d("LOCAL_DATABASE", "hehe, yes! he is actually added to inner database");
                    }
                    else {
                        Log.d("LOCAL_DATABASE ", "USER is already here");
                    }
                    presenter.OnSuccess(json_body);

                }
                else {
                    presenter.OnError("error");
                }

            }
        });
    }

    @Override
    public void SendPostRegister(String login, String password, String email, int RoleId) {
        UserLoginRegisterClass reg_user = new UserLoginRegisterClass(login, password, email, RoleId);
        //converting to json
        String json = new Gson().toJson(reg_user);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request = new Request.Builder()

                .url(RequestOptions.request_url_register)
                .post(formBody)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presenter.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200){


                    UserLoginAuthClass user = gson.fromJson(json_body, UserLoginAuthClass.class);
                    presenter.OnSuccess(json_body);

                }
                else {
                    presenter.OnError("error " + json_body);
                }

            }
        });
    }

    @Override
    public void SendGetAnimes(String search, int page) {

        //converting to json

        FeedUserLocal user = db_helper.CheckIfExist();

        Request request = new Request.Builder()
                .url(String.format("http://192.168.0.105/api/Anime/GetAnimes?page=%o&search=%s",page, search))
                .get()
                .addHeader("Authorization", "Bearer " + user.Token )
                .build();
        Call call = client.newCall(request);

        /*Log.v("headers", )*/

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presenter.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    String response_body = response.body().string();

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.registerTypeAdapter(Date.class, new DateDeserializer()).create();
                    AnimeResponse response_animes = gson.fromJson(response_body, AnimeResponse.class);

                    presenter.OnSuccess("Поиск успешен.", response_animes.animes, response_animes.currentPage, response_animes.pages);
                }
                else {


                    presenter.OnError(response.message());
                }
            }
        });

    }

    @Override
    public void SendGetAnime(int id) {


        FeedUserLocal user = db_helper.CheckIfExist();


        //converting to json
        Request request = new Request.Builder()
                .url(String.format("http://192.168.0.105/api/Anime/GetAnime?id=%o", id  ))
                .get()
                .addHeader("Authorization", user.Token)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presenter.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    String response_body = response.body().string();

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.registerTypeAdapter(Date.class, new DateDeserializer()).create();
                    Anime anime = gson.fromJson(response_body, Anime.class);

                    presenter.OnSuccess(anime);
                }
                else {
                    presenter.OnError("Что-то явно пошло не так.");
                }
            }
        });

    }

    public  static String ConvertToJsonByGson(Object obj){
        return new Gson().toJson(obj);
    }


}
