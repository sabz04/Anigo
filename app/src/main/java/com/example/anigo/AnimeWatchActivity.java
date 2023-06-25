package com.example.anigo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.example.anigo.Activities.MainActivityLogic.MainActivity;
import com.example.anigo.DialogHelper.CreateErrorContactDialog;
import com.example.anigo.Models.AnimeResponse;
import com.example.anigo.Models.Result;
import com.example.anigo.Models.Root;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnimeWatchActivity extends AppCompatActivity {

    Bundle webViewState;

    WebView watchWebView;

    OkHttpClient client;
    FrameLayout fullScreenContainer;

    CreateErrorContactDialog errorContactDialog;

    String token = "87b94eff12d9e9e0ec87798bc715a8af";

    String htmlString = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title></title><style>html,body{height:100%;display:flex;justify-content:center;align-items:center;}</style></head><body><h1>На данный момент по этому тайтлу нет материалов.</h1></body></html>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_watch);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("animeId");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        client = new OkHttpClient();
        watchWebView = findViewById(R.id.watchWebView);

        if(savedInstanceState != null){
            watchWebView.restoreState(savedInstanceState.getBundle("web"));
            return;
        }
        watchWebView.getSettings().setJavaScriptEnabled(true);
        watchWebView.getSettings().setAllowContentAccess(true);
        watchWebView.getSettings().setAllowFileAccess(true);

        WebChromeClient webChromeClient = new WebChromeClient() {

            // Вызывается, когда WebView переходит в полноэкранный режим
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);

                // Создаем новый контейнер для WebView, который будет занимать весь экран
                fullScreenContainer = new FrameLayout(AnimeWatchActivity.this);
                fullScreenContainer.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                // Добавляем новый контейнер на место старого
                ViewGroup parent = (ViewGroup) watchWebView.getParent();
                parent.addView(fullScreenContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                // Скрываем старый WebView
                watchWebView.setVisibility(View.GONE);
            }

            // Вызывается, когда WebView заканчивает работу в полноэкранном режиме
            @Override
            public void onHideCustomView() {
                super.onHideCustomView();

                // Удаляем контейнер для WebView, занимавший весь экран
                ViewGroup parent = (ViewGroup) fullScreenContainer.getParent();
                parent.removeView(fullScreenContainer);

                // Показываем старый WebView
                watchWebView.setVisibility(View.VISIBLE);
            }
        };
        watchWebView.setWebChromeClient(webChromeClient);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        Request request = new Request.Builder()
                .url(String.format(RequestOptions.request_url_kodik_watch_anime, token, id))
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    Root animeResponse = new Gson().fromJson(json_body, Root.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (animeResponse.results.size() < 1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        watchWebView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
                                    }
                                });

                            }else{
                                watchWebView.loadUrl(animeResponse.results.get(0).link);

                            }
                        }
                    });
                }
                else {

                }

            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Сохраните состояние WebView
        Bundle webViewBundle = new Bundle();
        watchWebView.saveState(webViewBundle);
        outState.putBundle("web", webViewBundle);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Восстановите состояние WebView
        webViewState = savedInstanceState.getBundle("web");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webViewState != null){
            watchWebView.restoreState(webViewState);
        }
    }
}