package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

public interface ViewHolderContract {
    interface View{
        void OnSuccessAddLike(String message);
        void OnErrorAddLike(String message);
        void OnSuccessRemoveComment(String message);
        void OnErrorRemoveComment(String message);
    }
    interface ILikeAdd{
        void AddLike(int commentId);
    }
    interface ILikeRemove{
        void RemoveLike();
    }

}
