package com.example.anigo;

import android.content.Context;

public class AnimeActivityPresenter implements AnimeActivityContract.Presenter, OkHttpContract.Presenter {

    AnimeActivityContract.View view;

    OkHttpUserHelper okHttpUserHelper;

    Context context;

    public AnimeActivityPresenter(AnimeActivityContract.View view) {
        this.view = view;

    }

    @Override
    public void GetAnime(int id) {

        okHttpUserHelper = new OkHttpUserHelper(this);

        okHttpUserHelper.SendGetAnime(id);
    }

    @Override
    public void OnSuccess(String message) {

    }

    @Override
    public void OnSuccess(String message, Anime[] animes, int current_page, int pages_count) {

    }

    @Override
    public void OnError(String message) {

    }

    @Override
    public void OnSuccess(Anime anime) {

    }
}
