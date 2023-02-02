package com.example.anigo;

import static android.content.ContentValues.TAG;

import android.content.Context;
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

    ArrayList<Anime>  animes_pagination = new ArrayList<>();

    int last_seen_elem = -1;
    int current_page = 1;

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
        if (savedInstanceState != null) {
            savedInstanceState = savedInstanceState.getBundle("FRAGMENT_SEARCH");
            Log.d("bundle_check: ",savedInstanceState.getString("Key_string"));
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (savedInstanceState != null) {
            savedInstanceState = savedInstanceState.getBundle("FRAGMENT_SEARCH");
            *//*savedInstanceState.getBundle("FRAGMENT_SEARCH");*//*
            Log.d("bundle_check: ",savedInstanceState.getString("Key_string"));
        }*/
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle("FRAGMENT_SEARCH", outState);
        outState.putString("Key_string", "it is key from the saved bundle!");
        outState.putBundle("FRAGMENT_SEARCH",outState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;

        view = NavigationActivity.search_fragment_instance;
        if(view == null)
            view = inflater.inflate(R.layout.fragment_search, container, false);
        // save the view to parent activity

        NavigationActivity.search_fragment_instance = view;

        current_view = view;
        editText_search = (EditText) view.findViewById(R.id.edit_search);

        swp = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        presenter = new SearchFragmentPresenter(this);

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
                        presenter.Search(editText_search.getText().toString(), current_page);


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
                presenter.Search(editText_search.getText().toString(), current_page);
            }
        });








      
        /*String search ="";
        TextView tViewSearch = view.findViewById(R.id.edit_search);
        tViewSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Search = tViewSearch.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Search = tViewSearch.getText().toString();
            }
        });


        tViewSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                System.out.println(actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                        ) {
                    RunnableRefreshListener.run();
                    return true;
                }
                return false;
            }



        });




        RunnableRefreshListener = new Runnable() {
            @Override
            public void run() {

                if(Search.equals("")){

                    swp.setRefreshing(false);
                    return;
                }
                swp.setRefreshing(true);
                String url = "http://192.168.0.106:560/anigo/animes?search="+Search;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                ClientClass clientClass = new ClientClass(request, url);
                Handler mainHandler = new Handler(view.getContext().getMainLooper());
                clientClass.setMainHandler(mainHandler);

                Runnable updateRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Anime[] animes = clientClass.ClientGetAnimes();
                        GridView grd = (GridView) view.findViewById(R.id.gridView);

                        GridAdapter gridAdapter = new GridAdapter(SearchFragment.this.getContext(),animes);
                        grd.setAdapter(gridAdapter);
                        swp.setRefreshing(false);

                        grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Bundle bundle = new Bundle();

                                bundle.putSerializable("current_title", (Serializable) animes[i]);

                                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_fragmentAnime, bundle);

                                Toast.makeText(SearchFragment.this.getActivity(), animes[i].airedOn.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    };
                };
                clientClass.setOnResponseRunnable(updateRunnable);
                clientClass.ClientSendRequest();

            }
        };


        if(firstEnter){
            Search = "Cowboy";
            RunnableRefreshListener.run();
            firstEnter = false;
            Search = "";
        }




        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RunnableRefreshListener.run();
            }
        });
*/



        return view;
    }
    public void ClearPaginationConfig(){
        last_seen_elem=-1;
        animes_pagination.clear();
        current_page=1;
    }

    @Override
    public void onSuccess(String message, Anime[] animes, int currentpage, int pagecount) {
        //временно, потом придется current_page перенести в свайпрефреш событие
        Context context = current_view.getContext();

        current_page = currentpage+1;

        if (currentpage >= pagecount){
            swp.setRefreshing(false);
            return;
        }

        for (Anime item:animes) {
            animes_pagination.add(item);
        }


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                GridView grd = (GridView) current_view.findViewById(R.id.gridView);
                state = grd.onSaveInstanceState();
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
                            presenter.Search(editText_search.getText().toString(), current_page);;
                            last_seen_elem = last_seen;
                        }

                    }
                });

                Anime[] anime_array = new Anime[animes_pagination.size()];

                GridAdapter gridAdapter = new GridAdapter(SearchFragment.this.getContext(), animes_pagination.toArray(anime_array) );


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
