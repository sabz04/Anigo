package com.example.anigo;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class SearchFragment extends Fragment implements SearchFragmentContract.View {

    SearchFragmentContract.Presenter presenter;
    Parcelable state;
    SearchFragment searchFragment;

    int last_seen_elem = -1;
    int current_page = 1;
    int page_count = -1;
    String search_text = "";

    EditText editText_search;

    Button download_data_btn;

    SwipeRefreshLayout swp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View current_view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  Runnable RunnableRefreshListener = null;
    private static boolean firstEnter = true;
    private OkHttpClient clientOk = new OkHttpClient();

    private  static  String Search ="";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(savedInstanceState == null)
            return;
        /*animes_pagination = savedInstanceState.getParcelableArrayList("animes_pagination");*/
        current_page = savedInstanceState.getInt("current_page");

        page_count = savedInstanceState.getInt("page_count");

        search_text = savedInstanceState.getString("editText_search");

        state = savedInstanceState.getParcelable("grid");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("editText_search", search_text);

        /*outState.putParcelableArrayList("animes_pagination", animes_pagination);*/

        outState.putInt("current_page", current_page);

        outState.putInt("page_count", page_count);

        outState.putParcelable("grid", state);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter = new SearchFragmentPresenter(this,getContext());
        View view;

        /*view = NavigationActivity.search_fragment_instance;
        if(view == null)*/
            view = inflater.inflate(R.layout.fragment_search, container, false);
        // save the view to parent activity

    /*    NavigationActivity.search_fragment_instance = view;*/
        GridView grd = view.findViewById(R.id.gridView);
        grd.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int last_seen = grd.getLastVisiblePosition();

                if(last_seen == last_seen_elem)
                    return;

                if (last_seen >= totalItemCount-1){
                    swp.setRefreshing(true);
                    presenter.Search(editText_search.getText().toString(), current_page, getContext());;
                    last_seen_elem = last_seen;
                }

            }
        });
        grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent to_anime = new Intent(getActivity(),AnimeActivity.class);

                Bundle bundle = new Bundle();

                int id = NavigationActivity.animes_pagination.get(i).shikiId;
                bundle.putInt("id", id);

                to_anime.putExtras(bundle);

                startActivity(to_anime);

                //Toast.makeText(SearchFragment.this.getActivity(), animes[i].nextEpisodeAt.toString(), Toast.LENGTH_LONG).show();
            }
        });
        if(state != null) {
            Log.d(TAG, "trying to restore listview state");
            Anime[] anime_array = new Anime[NavigationActivity.animes_pagination.size()];

            GridAdapter gridAdapter = new GridAdapter(SearchFragment.this.getContext(), NavigationActivity.animes_pagination.toArray(anime_array) );

            grd.setAdapter(gridAdapter);

            grd.onRestoreInstanceState(state);
        }
        current_view = view;

        editText_search = (EditText) view.findViewById(R.id.edit_search);
        editText_search.setText(search_text);
        search_text = editText_search.getText().toString();

        swp = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);



        swp.setColorSchemeResources(R.color.nicered);


        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                System.out.println(actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                        ClearPaginationConfig();
                        swp.setRefreshing(true);
                        state=null;
                        presenter.Search(editText_search.getText().toString(), current_page, getContext());


                    return true;
                }
                return false;
            }
        });

        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ClearPaginationConfig();
                swp.setRefreshing(true);
                presenter.Search(editText_search.getText().toString(), current_page, getContext());
            }
        });

        return view;
    }
    public void ClearPaginationConfig(){
        last_seen_elem=-1;
        NavigationActivity.animes_pagination.clear();
        current_page=1;
        page_count=-1;
        state =null;
    }

    @Override
    public void onSuccess(String message, Anime[] animes, int currentpage, int pagecount) {
        //временно, потом придется current_page перенести в свайпрефреш событие
        Context context = current_view.getContext();
        this.page_count = pagecount;
        this.current_page = currentpage+1;

        if (currentpage >= pagecount){
            swp.setRefreshing(false);
            return;
        }

        for (Anime item:animes) {
            NavigationActivity.animes_pagination.add(item);
        }

        if(getActivity() == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                GridView grd = (GridView) current_view.findViewById(R.id.gridView);
                state = grd.onSaveInstanceState();





                Anime[] anime_array = new Anime[NavigationActivity.animes_pagination.size()];

                GridAdapter gridAdapter = new GridAdapter(SearchFragment.this.getContext(), NavigationActivity.animes_pagination.toArray(anime_array) );


                grd.setAdapter(gridAdapter);

                if(state != null) {
                    Log.d(TAG, "trying to restore listview state");
                    grd.onRestoreInstanceState(state);
                }
                swp.setRefreshing(false);

            }
        });
    }

    @Override
    public void onSuccess(String message) {
        Context context = current_view.getContext();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onError(String message, String body) {
        Context context = current_view.getContext();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onError(String message) {
        Context context = current_view.getContext();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
