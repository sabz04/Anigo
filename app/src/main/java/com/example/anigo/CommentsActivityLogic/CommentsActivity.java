package com.example.anigo.CommentsActivityLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic.CommentDeletePresenter;
import com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic.CommentLikePresenter;
import com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic.CommentsAdapter;
import com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic.LikeRemovePresenter;
import com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic.ViewHolderContract;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.Models.Anime;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.AnimeCommentResponse;
import com.example.anigo.R;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity implements CommentsActivityContract.View, ViewHolderContract.View {

    CommentsActivityPresenter presenter;

    RecyclerView commentsRecycler;
    Button backButton;
    Button downloadButton;
    Button gridLikeButton;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    Parcelable recyclerViewScrollState;

    CommentLikePresenter likePresenter;
    CommentDeletePresenter deletePresenter;
    LikeRemovePresenter likeRemovePresenter;

    CommentsAdapter recyclerViewAdapter;

    int currentPage = 1;
    int pageCount=-1;
    int lastVisiblePosition=-1;
    int animeId;
    int lastPage = -1;


    int adapterPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        //get animeId from another activity
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("animeId");
        this.animeId = id;
        //getcontext
        context = this;
        //presenter declaration
        presenter = new CommentsActivityPresenter(this,context);
        presenter.GetComments(currentPage, animeId);
        //declate the presenters
        likePresenter = new CommentLikePresenter(this, this);
        deletePresenter = new CommentDeletePresenter(this, this);
        likeRemovePresenter = new LikeRemovePresenter(this, this);
        //get UI elems
        backButton = findViewById(R.id.backButton);

        commentsRecycler = findViewById(R.id.commentsRecyclerView);
        //swipeRefreshLayout = findViewById(R.id.swiperefresh);
        //swipeRefreshLayout.setRefreshing(true);
        //set the actions
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastVisiblePosition = -1;
                swipeRefreshLayout.setRefreshing(true);
                NavigationActivity.CommentsPagination.clear();
                currentPage = 1;
                recyclerViewAdapter = null;
                presenter.GetComments(currentPage, animeId);
            }
        });*/
        commentsRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManagerRecycler = (LinearLayoutManager)commentsRecycler.getLayoutManager();
                lastVisiblePosition = layoutManagerRecycler.findLastVisibleItemPosition();
                int itemCount = layoutManagerRecycler.getItemCount();

                if(lastVisiblePosition >= itemCount-1){
                    recyclerViewScrollState = layoutManagerRecycler.onSaveInstanceState();

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
            if(!CheckIfExist(comment.id)){
                NavigationActivity.CommentsPagination.add(comment);
                Log.wtf("added_comment", String.valueOf(comment.id));
            }
        }
        if(recyclerViewAdapter == null){
            recyclerViewAdapter = new CommentsAdapter(NavigationActivity.CommentsPagination,userId, context);
            recyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
            recyclerViewAdapter.setLikeClickListener(new CommentsAdapter.likeButtonItemClickListener() {
                @Override
                public void onItemLikeClick(int position, Button likeButton) {
                    likePresenter.AddLike(recyclerViewAdapter.getData().get(position).id,position);
                    adapterPosition = position;
                    gridLikeButton = likeButton;
                }
            });
            recyclerViewAdapter.setLoadClickListener(new CommentsAdapter.loadMoreButtonItemClickListener() {
                @Override
                public void loadMore() {
                    currentPage++;
                    presenter.GetComments(currentPage, animeId);
                }
            });
            recyclerViewAdapter.setDeleteClickListener(new CommentsAdapter.deleteButtonItemClickListener() {
                @Override
                public void onItemDeleteClick(int position) {
                    ArrayList<AnimeComment> commentsAnime = recyclerViewAdapter.getData();
                    /*AnimeComment comment = recyclerViewAdapter.getData().get(position);*/
                    AnimeComment com = NavigationActivity.CommentsPagination.get(position);
                    deletePresenter.RemoveComment(com.id);
                    adapterPosition = position;
                }
            });
            recyclerViewAdapter.setLikeRemoveClickListener(new CommentsAdapter.likeDeleteItemClickListener() {
                @Override
                public void likeDelete(int position) {
                    adapterPosition = position;
                    AnimeComment com = NavigationActivity.CommentsPagination.get(position);
                    likeRemovePresenter.RemoveLike(com.id);
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    commentsRecycler.setLayoutManager(new LinearLayoutManager(context));
                    commentsRecycler.setAdapter(recyclerViewAdapter);
                    //swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.getData().size()-1);
                    //swipeRefreshLayout.setRefreshing(false);
                }
            });
        }

    }
    public boolean CheckIfExist(int commentId){
        boolean ifExist = false;
        for (AnimeComment comm:
             NavigationActivity.CommentsPagination) {
            if(comm.id != commentId){
                ifExist = false;
            }
            else{
                return true;
            }
        }
        return ifExist;
    }
    @Override
    public void OnError(String message) {

    }
    @Override
    public void OnSuccessAddLike(AnimeComment commentAnime, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NavigationActivity.CommentsPagination.set(adapterPosition, commentAnime);
                recyclerViewAdapter.notifyItemChanged(adapterPosition);
            }
        });
    }
    @Override
    public void OnErrorAddLike(String message) {

    }
    AnimeComment removedComment;
    @Override
    public void OnSuccessRemoveComment(String message) {
        int removedId = Integer.valueOf(message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (AnimeComment comment:
                     recyclerViewAdapter.getData()) {
                    if(comment.id == removedId){
                        removedComment = comment;
                    }
                }
                NavigationActivity.CommentsPagination.remove(removedComment);
                recyclerViewAdapter.notifyItemRemoved(adapterPosition);
                recyclerViewAdapter.notifyItemRangeChanged(adapterPosition,  recyclerViewAdapter.getData().size()-1);
            }
        });
    }

    @Override
    public void OnErrorRemoveComment(String message) {

    }

    @Override
    public void OnSuccessRemoveLike(AnimeComment comment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NavigationActivity.CommentsPagination.set(adapterPosition, comment);
                recyclerViewAdapter.notifyItemChanged(adapterPosition);
            }
        });

    }

    @Override
    public void OnErrorRemoveLike(String message) {

    }
}