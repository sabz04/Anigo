package com.example.anigo.CommentsActivityLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anigo.Activities.AnimeActivityLogic.AnimeActivityPresenterAddComment;
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
    Button addCommentButton;
    Button downloadButton;
    Button gridLikeButton;
    Context context;
    EditText commentEditText;
    SwipeRefreshLayout swipeRefreshLayout;
    Parcelable recyclerViewScrollState;
    Spinner spinner;


    AnimeActivityPresenterAddComment addCommentPresenter;
    CommentLikePresenter likePresenter;
    CommentDeletePresenter deletePresenter;
    LikeRemovePresenter likeRemovePresenter;

    CommentsAdapter recyclerViewAdapter;

    int currentPage = 1;
    int pageCount=-1;
    int lastVisiblePosition=-1;
    int animeId;
    int lastPage = -1;

    String[] sortNames = {"Сначала новее", "Популярные"};
    String sortKey = "Сначала новее";

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
        presenter.GetComments(currentPage, animeId, sortKey);
        //declate the presenters
        likePresenter = new CommentLikePresenter(this, this);
        deletePresenter = new CommentDeletePresenter(this, this);
        likeRemovePresenter = new LikeRemovePresenter(this, this);
        addCommentPresenter = new AnimeActivityPresenterAddComment(this, this);
        //get UI elems
        backButton = findViewById(R.id.backButton);
        addCommentButton = findViewById(R.id.add_comment_btn);
        commentEditText = findViewById(R.id.commentEditText);
        commentsRecycler = findViewById(R.id.commentsRecyclerView);
        spinner = findViewById(R.id.spinnerSortValues);
        //swipeRefreshLayout = findViewById(R.id.swiperefresh);
        //swipeRefreshLayout.setRefreshing(true);


        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sortNames);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                if(item.contains("Сначала новее")){
                    sortKey = "SortByDate";
                    currentPage = 1;
                    NavigationActivity.CommentsPagination.clear();
                    recyclerViewAdapter = null;
                    presenter.GetComments(currentPage, animeId, sortKey);
                }
                if(item.contains("Популярные")){
                    sortKey = "SortByLikes";
                    currentPage = 1;
                    NavigationActivity.CommentsPagination.clear();
                    recyclerViewAdapter = null;
                    presenter.GetComments(currentPage, animeId, sortKey);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        };

        spinner.setOnItemSelectedListener(itemClickListener);

        //set the actions
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = commentEditText.getText().toString();
                String endStr = commentText.trim();
                if(endStr.isEmpty()){
                    Toast.makeText(context, "Пустая строка..", Toast.LENGTH_SHORT).show();
                    return;
                }
                addCommentPresenter.AddComment(commentText, animeId);
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
                    presenter.GetComments(currentPage, animeId, sortKey);
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
                    recyclerViewAdapter.notifyItemRangeChanged(
                            (NavigationActivity.CommentsPagination.size()-1)
                            -response.comments.length, response.comments.length);
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
    public void OnSuccessAddComment(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                currentPage = 1;
                recyclerViewAdapter = null;
                NavigationActivity.CommentsPagination.clear();
                presenter.GetComments(currentPage,animeId, sortKey);
                addCommentButton.setEnabled(false);
                addCommentButton.setBackground(getResources().getDrawable(R.drawable.baseline_schedule_send_24)); // Устанавливаем новый drawable для неактивной кнопки


                new CountDownTimer(60 * 1000, 1000) { // 5 минут в миллисекундах, 1000 миллисекунд (1 секунда) интервал
                    public void onTick(long millisUntilFinished) {
                        // Ничего не делаем на промежуточных тиках, если нужно, можно обновить интерфейс
                    }

                    public void onFinish() {
                        addCommentButton.setBackground(getResources().getDrawable(R.drawable.ic_baseline_send_24)); // Устанавливаем новый drawable для неактивной кнопки
                        addCommentButton.setEnabled(true); // Включаем кнопку по истечении таймера
                    }
                }.start();
            }
        });

    }

    @Override
    public void OnErrorAddComment(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
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