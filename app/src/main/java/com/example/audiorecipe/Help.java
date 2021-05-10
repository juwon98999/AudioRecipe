package com.example.audiorecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Help extends AppCompatActivity {

    ImageButton backbtn; //메인화면으로 화면전환

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        ActionBar actionBar = getSupportActionBar(); //액션바 숨기기
        actionBar.hide();

        backbtn = (ImageButton) findViewById(R.id.mainbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }

}
