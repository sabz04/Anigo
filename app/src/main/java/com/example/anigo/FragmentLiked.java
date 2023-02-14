package com.example.anigo;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavOptions;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLiked#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLiked extends Fragment implements FragmentLikedContract.View{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentLikedPresenter presenter;
    private SwipeRefreshLayout swp;
    private View view;
    private Favourite[] favourites_response;
    private GridView grd;
    private static Parcelable scroll_state;
    private static int current_page=1;
    private static int last_seen_elem = -1;
    private static int page_count =-1;
    private ArrayList<Favourite> favourites_temp_cached = new ArrayList<>();
    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1 = "!";
    private String mParam2;

    public FragmentLiked() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLiked.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLiked newInstance(String param1, String param2) {
        FragmentLiked fragment = new FragmentLiked();
        Bundle args = new Bundle();

        args.putParcelable("grid_state", scroll_state);
        args.putInt("current_page", current_page);
        args.putInt("page_count", page_count);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scroll_state = getArguments().getParcelable("grid_state");
            current_page = getArguments().getInt("current_page");
            page_count = getArguments().getInt("page_count");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        scroll_state = grd.onSaveInstanceState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_liked, container, false);
        this.favourites_temp_cached = (ArrayList<Favourite>) NavigationActivity.favourites_pagination.clone();
        presenter = new FragmentLikedPresenter(this , getContext());
        swp = view.findViewById(R.id.swiperefresh);
        swp.setColorSchemeResources(R.color.nicered);
        grd = view.findViewById(R.id.gridView);
        context = getContext();

        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swp.setRefreshing(true);
                ClearPageConfig();
                presenter.GetFavs(current_page);
            }
        });

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
                    current_page++;
                    presenter.GetFavs(current_page);
                    last_seen_elem = last_seen;
                }
            }
        });
        grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent to_anime = new Intent(getActivity(),AnimeActivity.class);
                Bundle bundle = new Bundle();
                int id = NavigationActivity.favourites_pagination.get(i).anime.shikiId;
                bundle.putInt("id", id);
                to_anime.putExtras(bundle);
                startActivity(to_anime);
            }
        });

        FragmentLikedGridAdapter liked_adapter = new FragmentLikedGridAdapter(context, NavigationActivity.favourites_pagination);
        grd.setAdapter(liked_adapter);
        if(scroll_state != null)
            grd.onRestoreInstanceState(scroll_state);

        if(NavigationActivity.favourites_pagination.size() < 1){
            swp.setRefreshing(true);
            presenter.GetFavs(current_page);
        }

        return view;
    }
    private void ClearPageConfig(){
        this.current_page = 1;
        NavigationActivity.favourites_pagination.clear();
        scroll_state = null;
        last_seen_elem = -1;
    }
    @Override
    public void OnSuccess(Favourite[] favourites, int current_page, int page_count) {

        this.current_page = current_page;
        this.page_count = page_count;

        if(current_page > page_count){
            swp.setRefreshing(false);
            return;
        }

        for(Favourite fav : favourites){
                NavigationActivity.favourites_pagination.add(fav);
        }

        if(getActivity() == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scroll_state = grd.onSaveInstanceState();
                FragmentLikedGridAdapter adapter =
                        new FragmentLikedGridAdapter(
                                context,
                                NavigationActivity.favourites_pagination);
                grd.setAdapter(adapter);

                if(scroll_state != null)
                    grd.onRestoreInstanceState(scroll_state);

                swp.setRefreshing(false);
            }
        });
    }

    @Override
    public void OnError(String message) {
        swp.setRefreshing(false);
    }
}