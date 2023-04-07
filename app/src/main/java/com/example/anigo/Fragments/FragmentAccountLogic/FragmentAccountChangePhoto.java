package com.example.anigo.Fragments.FragmentAccountLogic;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.Models.User;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentAccountChangePhoto implements FragmentAccountContract.PresenterChangePhoto, AuthentificationInterface.Listener {

    private FragmentAccountContract.View view;
    private Authentification auth;
    private Context context;
    private OkHttpClient client;

    Uri uriFile;

    public FragmentAccountChangePhoto(FragmentAccountContract.View view, Context context) {
        this.view = view;
        this.context = context;
        auth = new Authentification(this, context);
        client = new OkHttpClient();
    }

    @Override
    public void ChangePhoto(Uri file) {
        this.uriFile = file;
        auth.Auth();
    }

    @Override
    public void AuthSuccess(String message) {

    }

    @Override
    public void AuthError(String message) {

    }
    public String getRealPathFromURI(Uri uri) {
        String filePath = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                filePath = cursor.getString(index);
                cursor.close();
            }
        }
        return filePath;
    }
    @Override
    public void AuthSuccess(String token, int user_id) {

        File file = new File(getRealPathFromURI(uriFile));
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))
                .build();

        Request request = new Request.Builder()
                .url(RequestOptions.request_url_change_photo +user_id)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnErrorChangePhoto(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    view.OnSuccessChangePhoto(json_body);
                }
                else {
                    view.OnErrorChangePhoto(json_body);
                }

            }
        });
    }
}
