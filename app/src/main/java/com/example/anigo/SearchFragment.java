package com.example.anigo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<String> anime_names = new ArrayList<>();
    private ArrayList<String> anime_descs = new ArrayList<>();
    private ArrayList<Bitmap> anime_images = new ArrayList<>();

    private  static  String Search ="";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        if(MainActivity.view == null){
             view = inflater.inflate(R.layout.fragment_search, container, false);
        }
        else{
            view = MainActivity.view;
        }
        EditText editText_search = (EditText) view.findViewById(R.id.edit_search);
        SwipeRefreshLayout swp = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        swp.setColorSchemeResources(R.color.nicered);
        OkHttpClient client = new OkHttpClient();
        String search ="";
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


        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                anime_descs.clear();
                anime_images.clear();
                anime_names.clear();
                if(Search=="")
                    return;
                String url = "http://192.168.0.106:560/anigo/animes?search="+Search;

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        try {
                            String resp = response.body().string();
                            System.out.println(resp);
                            JSONArray jAnimesArray = new JSONArray(resp);

                            for(int i =0; i< jAnimesArray.length();i++){
                                String anime_name = jAnimesArray.getJSONObject(i).getString("nameRus");

                                String anime_desc = jAnimesArray.getJSONObject(i).getString("description");

                                if(anime_desc.length()>150)
                                    anime_desc = anime_desc.substring(0, 150) + "...";

                                JSONArray anime_imagess =  new JSONArray(jAnimesArray.getJSONObject(i).getString("images"));

                                System.out.println(anime_name);
                                byte[] anime_poster = java.util.Base64.getDecoder().decode(anime_imagess.getJSONObject(0).getString("preview"));
                                anime_descs.add(anime_desc);
                                anime_names.add(anime_name);
                                anime_images.add(GetImageBitmap(anime_poster));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SearchFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GridView grd = (GridView) view.findViewById(R.id.gridView);
                                String[] names = {};
                                String[] descs = {};
                                Bitmap[] images = {};
                                GridAdapter gridAdapter = new GridAdapter(SearchFragment.this.getContext(),anime_names.toArray(names),anime_descs.toArray(descs), anime_images.toArray(images));
                                grd.setAdapter(gridAdapter);
                                swp.setRefreshing(false);
                            }
                        });

                    }
                });


            }
        });

//        GridView grd = (GridView) view.findViewById(R.id.gridView);
//
//        String[] names = {"Ковбой бибоп", "Ковбой бибоп", "Ковбой бибоп", "Ковбой бибоп"};
//        int[] ids = {R.drawable.test, R.drawable.test, R.drawable.test, R.drawable.test};
//
//        GridAdapter gridAdapter = new GridAdapter(SearchFragment.this.getContext(),anime_names.toArray(names),ids);
//        grd.setAdapter(gridAdapter);
        MainActivity.view = view; //кэширование?
        return view;
    }
    private Bitmap GetImageBitmap(byte[] jsonImage){

        return BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);

    }
}