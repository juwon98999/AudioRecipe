package com.example.audiorecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.audiorecipe.Recipe.Remen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Sign2 extends AppCompatActivity {

    Button agreeBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign2);
        ActionBar actionBar = getSupportActionBar(); //액션바 숨기기
        actionBar.hide();


        agreeBtn2 = (Button) findViewById(R.id.okbtn2);

        agreeBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Remen.class);
                startActivity(intent);

            }
        });

    }
}