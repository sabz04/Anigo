package com.example.anigo.Activities.AnimeActivityLogic;

import com.example.anigo.Models.Anime;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.AnimeResponseWithCommentCount;
import com.example.anigo.Models.CheckUserAcvitity;
import com.example.anigo.Models.RateResponse;
import com.example.anigo.Models.Screenshot;

public interface AnimeActivityContract {
    interface View {
        void OnSuccess(AnimeResponseWithCommentCount animeResponseWithCommentCount, int userId);
        void OnError(String message);
        void OnSuccess(String message);
        void OnSuccess(Screenshot[] screenshots);
        void OnSuccessCheck(CheckUserAcvitity checkUserAcvitity);
        void OnErrorCheck(CheckUserAcvitity checkUserAcvitity);
        void OnSuccessDelete(String deleted_message);
        void OnErrorDelete(String undeleted_message);
        void OnSuccessGetLinkedAnimes(Anime[] animes);
        void OnErrorGetLinkedAnimes(String message);
        void OnSuccessSetRate(RateResponse response);
        void OnErrorSetRate(String message);
    }
    interface PresenterGetLinkedAnimes{
        void GetAnimes(String frName);
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
    interface PresenterSetRating{
        void SetRating(int animeId, int rateValue);
    }
}
