package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

import com.example.anigo.Models.AnimeComment;

public interface ViewHolderContract {
    interface View{
        void OnSuccessAddLike(AnimeComment commentAnime, int position);
        void OnErrorAddLike(String message);
        void OnSuccessRemoveComment(String message);
        void OnErrorRemoveComment(String message);
        void OnSuccessRemoveLike(AnimeComment comment);
        void OnErrorRemoveLike(String message);

    }
    interface ILikeAdd{
        void AddLike(int commentId, int adapterPosition );
    }

    interface ICommentDelete{
        void RemoveComment(int commentId);
    }
    interface ILikeRemove{
        void RemoveLike(int commentId);
    }

}
