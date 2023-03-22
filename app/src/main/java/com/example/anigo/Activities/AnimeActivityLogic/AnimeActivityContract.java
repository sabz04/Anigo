package com.example.anigo.Activities.AnimeActivityLogic;

import com.example.anigo.Models.Anime;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.Screenshot;

public interface AnimeActivityContract {
    interface View {
        void OnSuccess(Anime anime);
        void OnError(String message);
        void OnSuccess(String message);
        void OnSuccess(Screenshot[] screenshots);
        void OnSuccessCheck(String msg_is_has);
        void OnErrorCheck(String msg_is_has);
        void OnSuccessDelete(String deleted_message);
        void OnErrorDelete(String undeleted_message);
        void OnSuccessGetComments(
                int pages, int currentPage, int currentPageItemCount,AnimeComment[] listComments, int userId);
        void OnErrorGetComments(String errorMessage);
        void OnSuccessAddComment(String message);
        void OnErrorAddComment(String message);
    }
    interface Presenter{
        void GetAnime(int id);
    }
    interface PresenterAddToFavs{
        void FavsAdd(String comment, int anime_id);
    }
    interface PresenterCheckIfExist{
        void Check(int anime_id);
    }
    interface PresenterDeleteFromFav{
        void Delete(int anime_id);
    }
    interface PresenterGetComments{
        void GetComments(int animeId);
    }
    interface PresenterAddComment{
        void AddComment(String comment, int animeId);
    }
}
