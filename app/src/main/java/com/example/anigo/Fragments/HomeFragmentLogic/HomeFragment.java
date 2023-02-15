package com.example.anigo.Fragments.HomeFragmentLogic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.anigo.Activities.AnimeActivityLogic.AnimeActivity;
import com.example.anigo.GridAdaptersLogic.GridAdapter;
import com.example.anigo.Models.Anime;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeFragmentContract.View{

    private HomeFragmentPresenter presenter;
    private static Parcelable state;
    private SwipeRefreshLayout swp;
    private GridView grd_animes;
    private GridAdapter grid_adapter;
    private View current_view;
    private Context context;

    private static int current_page=1, last_seen_elem = -1, page_count = -1;

    public HomeFragment() {

    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("grid_state", state);
        args.putInt("current_page", current_page);
        args.putInt("page_count", page_count );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            current_page = getArguments().getInt("current_page", current_page);
            page_count = getArguments().getInt("page_count", page_count);
            state = getArguments().getParcelable("grid_state");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        state = grd_animes.onSaveInstanceState();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        current_view =inflater.inflate(R.layout.fragment_home, container, false);

        grd_animes = current_view.findViewById(R.id.gridView);
        swp = current_view.findViewById(R.id.swiperefresh);
        swp.setColorSchemeResources(R.color.nicered);

        context = getContext();
        presenter = new HomeFragmentPresenter(this, context);

        grd_animes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent to_anime = new Intent(getActivity(), AnimeActivity.class);
                Bundle bundle = new Bundle();
                int id = NavigationActivity.animes_pagination_popular.get(i).shikiId;
                bundle.putInt("id", id);
                to_anime.putExtras(bundle);
                startActivity(to_anime);
            }
        });
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swp.setRefreshing(true);
                ClearPageConfig();
                presenter.GetFavs(current_page);
            }
        });

        grd_animes.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int totalItemCount) {
                int last_seen = grd_animes.getLastVisiblePosition();

                if(last_seen == last_seen_elem)
                    return;

                if (last_seen >= totalItemCount-1){
                    swp.setRefreshing(true);
                    current_page++;
                    presenter.GetFavs(current_page);
                    last_seen_elem = last_seen;
                }
            }
        });

        grid_adapter = new GridAdapter(context, NavigationActivity.animes_pagination_popular);
        grd_animes.setAdapter(grid_adapter);
        if(state != null){
            grd_animes.onRestoreInstanceState(state);
            return current_view;
        }

        if(NavigationActivity.animes_pagination_popular.size() < 1){
            swp.setRefreshing(true);
            presenter.GetFavs(current_page);
        }

        return current_view;
    }

    private void ClearPageConfig(){
        this.current_page = 1;
        NavigationActivity.animes_pagination_popular.clear();
        state = null;
        last_seen_elem = -1;
        //grd.onRestoreInstanceState(null);
    }
    @Override
    public void OnSuccess(String message) {

    }

    @Override
    public void OnSuccess(Anime[] animes, int current_page, int page_count) {
        this.page_count = page_count;
        this.current_page = current_page;

        if (current_page > page_count){
            swp.setRefreshing(false);
            return;
        }

        for (Anime item:animes) {
            NavigationActivity.animes_pagination_popular.add(item);
        }

        if(getActivity() == null){
            swp.setRefreshing(false);
            return;
        }

        RestoreGridView();
    }
    private void RestoreGridView(){
        state = grd_animes.onSaveInstanceState();
        grid_adapter = new GridAdapter(getContext(), NavigationActivity.animes_pagination_popular);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                grd_animes.setAdapter(grid_adapter);

                if(state != null)
                    grd_animes.onRestoreInstanceState(state);

                swp.setRefreshing(false);
            }
        });
    }
    @Override
    public void OnError(String error) {

    }
}