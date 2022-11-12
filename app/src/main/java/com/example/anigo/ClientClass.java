package com.example.anigo;

import android.os.Handler;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClientClass {


    OkHttpClient client;

    Request request;

    String url;

    Anime[] animes;
    Runnable  runnable;
    Handler mainHandler;
    public ClientClass( Request request, String url ) {

        this.url = url;
        this.request = request;

        client = new OkHttpClient();

    }

    public void setOnResponseRunnable(Runnable  runnable){
        this.runnable = runnable;
    }

    public void setMainHandler(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public void ClientSendRequest(){
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.registerTypeAdapter(Date.class, new DateDeserializer()).create();
                Anime[] anime = gson.fromJson(resp, Anime[].class);
                animes = anime;

                mainHandler.post(runnable);

            }
        });
    }
    public Anime[] ClientGetAnimes(){
        return animes;
    }
}
