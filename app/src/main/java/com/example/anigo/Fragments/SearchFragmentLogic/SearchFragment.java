package com.example.anigo.Fragments.SearchFragmentLogic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.anigo.Activities.AnimeActivityLogic.AnimeActivity;
import com.example.anigo.GridAdaptersLogic.GridAdapter;
import com.example.anigo.Models.Anime;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.R;


public class SearchFragment extends Fragment implements SearchFragmentContract.View {

    private SearchFragmentContract.Presenter presenter;
    private static Parcelable state;

    private static int last_seen_elem = -1;
    private static int current_page = 1;
    private static int page_count = -1;
    private static String search_text = "";

    private SwipeRefreshLayout swp;
    private EditText editText_search;
    private GridView grd;
    private Context context;

    private View current_view;

    public SearchFragment() {

    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("editText_search", search_text);
        args.putInt("current_page", current_page);
        args.putInt("page_count", page_count);
        args.putParcelable("grid", state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)
            return;
        if (getArguments() != null) {
            current_page = getArguments().getInt("current_page");
            page_count = getArguments().getInt("page_count");
            search_text = getArguments().getString("editText_search");
            state = getArguments().getParcelable("grid");
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(grd != null){
            state = grd.onSaveInstanceState();
            search_text = editText_search.getText().toString();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        current_view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();
        presenter = new SearchFragmentPresenter(this,context);

        grd = current_view.findViewById(R.id.gridView);
        swp = (SwipeRefreshLayout) current_view.findViewById(R.id.swiperefresh);
        swp.setColorSchemeResources(R.color.nicered);

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
                    current_page++;
                    presenter.Search(editText_search.getText().toString(), current_page, context);;
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
                System.out.println(actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                        ClearPaginationConfig();
                        swp.setRefreshing(true);
                        presenter.Search(editText_search.getText().toString(), current_page, context);
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

        GridAdapter gridAdapter = new GridAdapter(context, NavigationActivity.animes_pagination );
        grd.setAdapter(gridAdapter);
        if(state != null)
            grd.onRestoreInstanceState(state);
        swp.setRefreshing(false);
        return current_view;
    }
    public void ClearPaginationConfig(){
        swp.setRefreshing(false);
        last_seen_elem=-1;
        NavigationActivity.animes_pagination.clear();
        current_page=1;
        page_count=-1;
        state =null;

    }

    @Override
    public void onSuccess(String message, Anime[] animes, int currentpage, int pagecount) {
        this.page_count = pagecount;
        this.current_page = currentpage;

        if (currentpage > pagecount){
            swp.setRefreshing(false);
            return;
        }

        for (Anime item:animes) {
            NavigationActivity.animes_pagination.add(item);
        }
        if(getActivity() == null){
            swp.setRefreshing(false);
            return;
        }

        state = grd.onSaveInstanceState();
        GridAdapter gridAdapter = new GridAdapter(
                context,
                NavigationActivity.animes_pagination);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                grd.setAdapter(gridAdapter);
                if(state != null) {
                    grd.onRestoreInstanceState(state);
                }
                swp.setRefreshing(false);
            }
        });
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
