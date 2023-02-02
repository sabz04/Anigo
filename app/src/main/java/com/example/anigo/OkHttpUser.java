package com.example.anigo;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUser implements Callback {

    private OkHttpClient client;
    private Callback call;
    private FeedUserDbHelper db_helper;
    private Context context;

    private static String ERROR_MESSAGE = "ERROR, FAIL TO SEND REQUEST";
    private static String SUCCESS_MESSAGE = "SUCCESS";
    public OkHttpUser(Context context) {
        this.context = context;
        db_helper = new FeedUserDbHelper(context);
    }
    public OkHttpUser(Context context, OkHttpClient client, Callback call) {
        this.context = context;
        this.call = call;
        this.client = client;
        db_helper = new FeedUserDbHelper(context);
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
