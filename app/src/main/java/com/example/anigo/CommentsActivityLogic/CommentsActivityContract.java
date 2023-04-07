package com.example.anigo.CommentsActivityLogic;

import com.example.anigo.Activities.AnimeActivityLogic.AnimeActivity;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.AnimeCommentResponse;

public interface CommentsActivityContract {
    interface View{
        void OnSuccessGetComments(AnimeCommentResponse response, int userId);
        void OnError(String message);
        void OnSuccessAddComment(String message);
        void OnErrorAddComment(String message);
    }
    interface Presenter{
        void GetComments(int currentPage, int animeId, String sortKey);
    }
}
