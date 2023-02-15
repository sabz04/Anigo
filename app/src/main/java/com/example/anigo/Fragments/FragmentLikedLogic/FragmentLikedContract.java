package com.example.anigo.Fragments.FragmentLikedLogic;

import com.example.anigo.Models.Favourite;

public interface FragmentLikedContract {
    interface View {
        void OnSuccess(Favourite[] favourite, int current_page, int page_count);
        void OnError(String message);
    }
    interface Presenter{
        void GetFavs(int page);
    }
}
