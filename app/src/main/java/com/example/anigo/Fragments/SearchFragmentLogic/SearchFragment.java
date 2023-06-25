package com.example.anigo.Fragments.SearchFragmentLogic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.anigo.Activities.AnimeActivityLogic.AnimeActivity;
import com.example.anigo.GridAdaptersLogic.FragmentLikedGridAdapter;
import com.example.anigo.GridAdaptersLogic.GridAdapter;
import com.example.anigo.Models.Anime;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.Models.AnimeFilterData;
import com.example.anigo.Models.AnimeResponse;
import com.example.anigo.Models.Favourite;
import com.example.anigo.Models.FilterObject;
import com.example.anigo.Models.StudiosResponse;
import com.example.anigo.R;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.example.anigo.UiHelper.FlowLayout;
import com.example.anigo.UiHelper.TextViewHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchFragment extends Fragment implements SearchFragmentContract.View {

    private SearchFragmentContract.Presenter presenter;
    private static Parcelable state;

    private static int last_seen_elem = -1;

    private static int page_count = -1;
    private static String search_text = "";
    private Button filterButton;

    private SwipeRefreshLayout swp;
    private EditText editText_search;
    private GridView grd;
    private Context context;
    private AlertDialog filterDialog;
    private FilterObject filterObject;
    private View current_view;
    private Spinner spinner;
    OkHttpClient client;
    private  int studiosPage = 1;

    String[] sortNames = {"Популярные", "Лучшие оценки"};
    public SearchFragment() {

    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){


            filterObject = (FilterObject) savedInstanceState.getSerializable("filters");

            page_count = savedInstanceState.getInt("page_count");
            search_text = savedInstanceState.getString("editText_search");
            state = savedInstanceState.getParcelable("grid");
        }
        if (getArguments() != null) {

        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(grd != null){
            state = grd.onSaveInstanceState();
            search_text = editText_search.getText().toString();
        }
        outState.putSerializable("filters", filterObject);
        outState.putString("editText_search", search_text);
        outState.putInt("page_count", page_count);
        outState.putParcelable("grid", state);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        current_view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();
        client = new OkHttpClient();
        if(filterObject == null){
            filterObject = new FilterObject();
            filterObject.Page = 1;
        }

        presenter = new SearchFragmentPresenter(this,context);

        grd = current_view.findViewById(R.id.gridView);
        swp = (SwipeRefreshLayout) current_view.findViewById(R.id.swiperefresh);
        swp.setColorSchemeResources(R.color.nicered);
        swp.setRefreshing(false);
        filterButton = current_view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewFilterDialog();
            }
        });
        if(state == null){
            swp.setRefreshing(true);
            presenter.Search(filterObject, context);;
        }






        editText_search = (EditText) current_view.findViewById(R.id.edit_search);
        editText_search.setText(search_text);

        grd.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int last_seen = grd.getLastVisiblePosition();

                if(last_seen == last_seen_elem || last_seen == -1)
                    return;

                if (last_seen >= totalItemCount-1){
                    swp.setRefreshing(true);
                    filterObject.Page++;
                    presenter.Search(filterObject, context);;
                    last_seen_elem = last_seen;
                }

            }
        });
        grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state = grd.onSaveInstanceState();
                Intent to_anime = new Intent(getActivity(), AnimeActivity.class);
                Bundle bundle = new Bundle();
                int id = NavigationActivity.animes_pagination.get(i).shikiId;
                bundle.putInt("id", id);
                to_anime.putExtras(bundle);
                startActivity(to_anime);

            }
        });

        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                                || actionId == EditorInfo.IME_ACTION_NEXT) {
                        ClearPaginationConfig();
                        swp.setRefreshing(true);
                        filterObject.Search = editText_search.getText().toString();
                        presenter.Search(filterObject, context);
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

                filterObject.Search = editText_search.getText().toString();
                presenter.Search(filterObject, getContext());
            }
        });

        _setGridAdapter(NavigationActivity.animes_pagination, true);

        return current_view;
    }

    private void CreateNewFilterDialog() {
        if(filterDialog!=null){
            filterDialog.show();
            return;
        }
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(getContext());
        View filterView = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        Button acceptButton = filterView.findViewById(R.id.acceptButton);
        FlowLayout genresLayout = filterView.findViewById(R.id.genresLayout);
        FlowLayout yearsLayout = filterView.findViewById(R.id.yearsLayout);
        FlowLayout typesLayout = filterView.findViewById(R.id.typesLayout);
        FlowLayout studiosLayout = filterView.findViewById(R.id.studiosLayout);
        Button getStudiosButton = filterView.findViewById(R.id.getStudiosButton);

        getStudiosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request = new Request.Builder()
                        .url(String.format(RequestOptions.request_url_get_studios,studiosPage))
                        .get()
                        .build();
                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonBody = response.body().string();
                        if(response.code() == 200){
                            studiosPage++;
                            StudiosResponse studiosResponse = new Gson().fromJson(jsonBody, StudiosResponse.class);
                            for (String studio:
                                    studiosResponse.studios){
                                CheckBox studioCheckBox = new CheckBox(getContext());
                                studioCheckBox.setText(studio);
                                studioCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if(b){
                                            filterObject.Studios.add(studio);
                                        } else {
                                            if(filterObject.Studios.contains(studio)){
                                                filterObject.Studios.remove(studio);
                                            }
                                        }
                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        studiosLayout.addView(studioCheckBox);
                                    }
                                });

                            }
                        }
                    }
                });
            }
        });

        spinner = filterView.findViewById(R.id.spinnerSortValues);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, sortNames);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                if(item.contains("Лучшие оценки")){
                    filterObject.SortKey = "SortByRate";
                }
                if(item.contains("Популярные")){
                    filterObject.SortKey = "SortByPopular";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        };
        spinner.setOnItemSelectedListener(itemClickListener);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearPaginationConfig();
                presenter.Search(filterObject, getContext());
                filterDialog.cancel();
            }
        });


        Request request = new Request.Builder()
                .url(String.format(RequestOptions.request_url_get_filter_data))
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response_body = response.body().string();
                if(response.code() == 200){
                    AnimeFilterData filterData = new Gson().fromJson(response_body, AnimeFilterData.class);

                    for (String genre:
                         filterData.genres) {
                        CheckBox genreCheckBox = new CheckBox(getContext());

                        genreCheckBox.setText(genre);
                        genreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked){
                                    filterObject.Genres.add(genre);
                                }
                                else {
                                    if(filterObject.Genres.contains(genre)){
                                        filterObject.Genres.remove(genre);
                                    }
                                }
                            }
                        });

                        genresLayout.addView(genreCheckBox);
                    }

                    for (int year:
                            filterData.years) {
                        String yearString = String.valueOf(year);
                        CheckBox yearCheckBox = new CheckBox(getContext());
                        yearCheckBox.setText(yearString);
                        yearCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(b){
                                    filterObject.Years.add(year);
                                } else {
                                    if(filterObject.Years.contains(year)){
                                        filterObject.Years.remove((Object)year);
                                        for (int i =0; i< filterObject.Years.size();i++) {
                                            if(filterObject.Years.get(i) == year){
                                                filterObject.Years.remove(i);
                                            }

                                        }
                                    }
                                }
                            }
                        });
                        yearsLayout.addView(yearCheckBox);
                    }
                    for (String type:
                            filterData.types) {
                        CheckBox typeCheckBox = new CheckBox(getContext());
                        typeCheckBox.setText(type);
                        typeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(b){
                                    filterObject.Type.add(type);
                                }
                                else {
                                    if(filterObject.Type.contains(type)){
                                        filterObject.Type.remove(type);
                                    }
                                }
                            }
                        });
                        typesLayout.addView(typeCheckBox);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            filterView.setClipToOutline(true);

                            dialog_builder.setView(filterView);
                            filterDialog = dialog_builder.create();

                            filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            filterDialog.show();
                        }
                    });

                }
                else {

                }
            }
        });



    }

    public void ClearPaginationConfig(){
        swp.setRefreshing(false);
        NavigationActivity.animes_pagination.clear();
        studiosPage=1;
        filterObject.Page=1;
        page_count=-1;
        state =null;
    }
    private void _setGridAdapter(ArrayList<Anime> ListAnimes, boolean isFirstEntered){
        if(!isFirstEntered){
            state = grd.onSaveInstanceState();
        }
        GridAdapter animesAdapter =
                new GridAdapter(
                        context,
                        ListAnimes);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                grd.setAdapter(animesAdapter);
                if(state != null)
                    grd.onRestoreInstanceState(state);
                swp.setRefreshing(false);
            }
        });
    }
    @Override
    public void onSuccess(String message, Anime[] animes, int currentpage, int pagecount) {
        this.page_count = pagecount;
        this.filterObject.Page = currentpage;

        for (Anime item:animes) {
            NavigationActivity.animes_pagination.add(item);
        }

        _setGridAdapter(NavigationActivity.animes_pagination, false);
        swp.setRefreshing(false);
    }

    @Override
    public void onSuccess(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swp.setRefreshing(false);
            }
        });
    }

    @Override
    public void onError(String message, String body) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swp.setRefreshing(false);
            }
        });
    }

    @Override
    public void onError(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swp.setRefreshing(false);
            }
        });
    }
}
