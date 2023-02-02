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

public class SearchFragmentPresenter implements  SearchFragmentContract.Presenter, OkHttpContract.Presenter{

    SearchFragmentContract.View view;

    OkHttpUserHelper presenter;

    public SearchFragmentPresenter(SearchFragmentContract.View view){this.view = view;}

    @Override
    public void Search(String search) {

    }

    @Override
    public void Search(String search, int page) {

        presenter =new OkHttpUserHelper(this);

        presenter.SendGetAnimes(search, page);

    }

    @Override
    public void OnSuccess(String message) {

    }

    @Override
    public void OnSuccess(String message, Anime[] animes, int current_page, int pages_count) {
        view.onSuccess(message, animes, current_page, pages_count);
    }

    @Override
    public void OnError(String message) {
        view.onError(message);
    }
}
