package com.example.anigo;


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

    FragmentLikedPresenter presenter;
    SwipeRefreshLayout swp;
    View view;
    Favourite[] favourites_response;
    GridView grd;
    Parcelable scroll_state;
    int current_page=1;
    int last_seen_elem = -1;
    int page_count =-1;

    // TODO: Rename and change types of parameters
    private String mParam1;
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
        if(savedInstanceState != null){
            if(scroll_state != null)
                scroll_state = savedInstanceState.getParcelable("grid");
            current_page = savedInstanceState.getInt("current_page");

            page_count = savedInstanceState.getInt("page_count");
        }


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        scroll_state = grd.onSaveInstanceState();
        outState.putParcelable("grid", scroll_state);

        outState.putInt("current_page", current_page);

        outState.putInt("page_count", page_count );

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_liked, container, false);
        presenter = new FragmentLikedPresenter(this , getContext());
        swp = view.findViewById(R.id.swiperefresh);
        swp.setColorSchemeResources(R.color.nicered);
        grd = view.findViewById(R.id.gridView);

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



        if(NavigationActivity.favourites_pagination.size() > 0){
            FragmentLikedGridAdapter adapter = new FragmentLikedGridAdapter(FragmentLiked.this.getContext(), NavigationActivity.favourites_pagination);

            grd.setAdapter(adapter);
            if(scroll_state != null)
                grd.onRestoreInstanceState(scroll_state);
        }
        else {
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
        //grd.onRestoreInstanceState(null);
    }
    @Override
    public void OnSuccess(Favourite[] favourites, int current_page, int page_count) {

        if(current_page > page_count){
            swp.setRefreshing(false);

            return;
        }
        this.current_page = current_page+1;
        this.page_count = page_count;
        this.favourites_response = favourites;



        for(Favourite fav : favourites){
            if(!CheckIfExistInCache(fav)){
                NavigationActivity.favourites_pagination.add(fav);
            }
        }
        FragmentLikedGridAdapter adapter = new FragmentLikedGridAdapter(FragmentLiked.this.getContext(), NavigationActivity.favourites_pagination);
        scroll_state = grd.onSaveInstanceState();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                grd = view.findViewById(R.id.gridView);


                grd.setAdapter(adapter);

                if(scroll_state != null)
                    grd.onRestoreInstanceState(scroll_state);

                swp.setRefreshing(false);
            }
        });

        String str = "";
    }
    public boolean CheckIfExistInCache(Favourite fav){
        for(int i =0; i< NavigationActivity.favourites_pagination.size();i++){
            Favourite fav_pag = NavigationActivity.favourites_pagination.get(i);
            if(fav_pag.anime.shikiId == fav.anime.shikiId){
                return true;
            }
        }
        return false;

    }
    public boolean CheckIfExistInUpdated(Favourite fav){
        for(int i =0; i< favourites_response.length;i++){
            Favourite fav_resp = favourites_response[i];
            if(fav_resp.anime.shikiId == fav.anime.shikiId){
                return true;
            }
        }
        return false;

    }

    @Override
    public void OnError(String message) {
        swp.setRefreshing(false);
    }
}