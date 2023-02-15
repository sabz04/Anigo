package com.example.anigo.Activities.CodeCheckActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;

public class CodeCheckActivityPresenter implements CodeCheckActivityContract.Presenter {



    Authentification authentification;

    CodeCheckActivityContract.View view;

    String code;

    String entered_code;

    Context context;

    public CodeCheckActivityPresenter(CodeCheckActivityContract.View view, Context context) {
        this.view = view;

        this.context = context;
    }
    @Override
    public void CheckCode(String code, String entered_code) {
        this.code = code;
        this.entered_code = entered_code;
        if(code.equals(entered_code)){
            view.OnSuccess("Успешно!");
        }
        else{
            view.OnError("Ошибка, коды не совпадают!");
        }
    }


}
