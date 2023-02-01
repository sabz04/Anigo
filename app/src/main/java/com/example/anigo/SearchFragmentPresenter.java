package com.example.anigo;

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

public class SearchFragmentPresenter implements  SearchFragmentContract.Presenter{

    SearchFragmentContract.View view;


    public SearchFragmentPresenter(SearchFragmentContract.View view){this.view = view;}

    @Override
    public void Search(String search) {

    }

    @Override
    public void Search(String search, int page) {

        OkHttpClient client = new OkHttpClient();

        //converting to json
        Request request = new Request.Builder()
                .url(String.format("http://192.168.0.105/api/Anime/GetAnimes?page=%o&search=%s",page, search))
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.onError("Ошибка запроса." + e.getMessage());
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
                    view.onError("Что-то явно пошло не так.", response.body().string());
                }
            }
        });

    }
}
