package com.example.audiorecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.audiorecipe.Recipe.Duruchigi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Sign3 extends AppCompatActivity {

    Button agreeBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign3);
        ActionBar actionBar = getSupportActionBar(); //액션바 숨기기
        actionBar.hide();


        agreeBtn3 = (Button) findViewById(R.id.okbtn3);

        agreeBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Duruchigi.class);
                startActivity(intent);

            }
        });

    }
}