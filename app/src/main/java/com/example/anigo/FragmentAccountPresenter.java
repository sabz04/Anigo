package com.example.anigo;

import android.content.Context;
import android.view.View;

public class FragmentAccountPresenter implements  FragmentAccountContract.Presenter{

    FragmentAccountContract.View view;

    public FragmentAccountPresenter(FragmentAccountContract.View view) {
        this.view = view;
    }

    @Override
    public void Exit(Context context) {
        FeedUserDbHelper db = new FeedUserDbHelper(context);

        FeedUserLocal user_local = db.CheckIfExist();

        if(user_local == null){
            view.onError("Пользователя не было в БД.");
            return;
        }
        else {
            db.Delete();
            view.onSuccess("Пользователь удален.");
        }

    }
}
