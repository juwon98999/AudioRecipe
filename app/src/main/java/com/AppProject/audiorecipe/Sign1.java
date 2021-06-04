package com.AppProject.audiorecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.AppProject.audiorecipe.Recipe.RecipeMusic;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Sign1 extends AppCompatActivity {

    ImageButton agreeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign1);
        ActionBar actionBar = getSupportActionBar(); //액션바 숨기기
        actionBar.hide();


        agreeBtn = (ImageButton) findViewById(R.id.okbtn);

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RecipeMusic.class);
                startActivity(intent);

            }
        });

    }
}
