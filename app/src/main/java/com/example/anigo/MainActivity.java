package com.example.anigo;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anigo.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button regBTn = findViewById(R.id.regBTN);
        Button loginbtn = findViewById(R.id.loginBTN);
        EditText login = findViewById(R.id.loginTB);
        EditText reg = findViewById(R.id.regTB);
        System.out.println(login.getText().toString());


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Snackbar mySnackbar = Snackbar.make(view, "Пожалуйста, введите все данные корректно!", 5000);
                    mySnackbar.setBackgroundTint(getResources().getColor(R.color.nicered));

                    mySnackbar.show();


            }
        });



        regBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                    startActivity(intent);
            }
        });
    }


}