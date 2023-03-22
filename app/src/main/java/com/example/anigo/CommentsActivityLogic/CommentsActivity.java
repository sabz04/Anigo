package com.example.anigo.CommentsActivityLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic.MyAdapter;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.AnimeCommentResponse;
import com.example.anigo.R;

public class CommentsActivity extends AppCompatActivity implements CommentsActivityContract.View{

    CommentsActivityPresenter presenter;

    RecyclerView commentsRecycler;
    Button backButton;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    Parcelable recyclerViewScrollState;

    MyAdapter recyclerViewAdapter;

    int currentPage = 1;
    int pageCount=-1;
    int lastVisiblePosition=-1;
    int animeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        //get animeId from another activity
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("animeId");
        this.animeId = id;
        //getcontext
        context = getApplicationContext();
        //presenter declaration
        presenter = new CommentsActivityPresenter(this,context);
        presenter.GetComments(currentPage, animeId);
        //get UI elems
        backButton = findViewById(R.id.backButton);
        commentsRecycler = findViewById(R.id.commentsRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);
        //set the actions
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastVisiblePosition = -1;
                swipeRefreshLayout.setRefreshing(true);
                NavigationActivity.CommentsPagination.clear();
                currentPage = 1;
                recyclerViewAdapter = null;
                presenter.GetComments(currentPage, animeId);
            }
        });
        commentsRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManagerRecycler = (LinearLayoutManager)commentsRecycler.getLayoutManager();
                lastVisiblePosition = layoutManagerRecycler.findLastVisibleItemPosition();
                int itemCount = layoutManagerRecycler.getItemCount();

                if(lastVisiblePosition >= itemCount-1){
                    recyclerViewScrollState = layoutManagerRecycler.onSaveInstanceState();
                    currentPage++;
                    presenter.GetComments(currentPage, animeId);
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        //lastVisiblePosition = ((LinearLayoutManager)commentsRecycler.getLayoutManager()).findLastVisibleItemPosition();
    }
    @Override
    public void onResume() {
        super.onResume();
        //((LinearLayoutManager)commentsRecycler.getLayoutManager()).scrollToPosition(lastVisiblePosition);
    }
    @Override
    public void OnSuccessGetComments(AnimeCommentResponse response, int userId) {


        if(response.comments.length < 1){
            return;
        }
        for (AnimeComment comment:
             response.comments) {
            NavigationActivity.CommentsPagination.add(comment);
        }
        if(recyclerViewAdapter == null){
            recyclerViewAdapter = new MyAdapter(this, NavigationActivity.CommentsPagination, userId);
            recyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    commentsRecycler.setLayoutManager(new LinearLayoutManager(context));
                    commentsRecycler.setAdapter(recyclerViewAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerViewAdapter.notifyItemInserted(NavigationActivity.CommentsPagination.size()-1);
                    swipeRefreshLayout.setRefreshing(false);

                }
            });
        }
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });*/
    }

    @Override
    public void OnError(String message) {

    }
}