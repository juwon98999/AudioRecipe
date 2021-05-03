package com.example.audiorecipe.Catejava;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.audiorecipe.R;
import com.example.audiorecipe.Recipe.RecipeMusic;

import androidx.appcompat.app.AppCompatActivity;

public class Catesetting extends AppCompatActivity {

    Button menurecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category1);

        menurecipe = (Button) findViewById(R.id.menu1);

        menurecipe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipeMusic.class);
                startActivity(intent);
            }
        });

        //액션바 설정하기//
        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle("카테고리 메뉴");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.

        if (id == R.id.item1) {

        }
        if (id == R.id.item2) {
            Intent settingIntent = new Intent(this, Catesetting2.class);
            startActivity(settingIntent);
        }
        if (id == R.id.item3) {
            Intent settingIntent = new Intent(this, Catesetting3.class);
            startActivity(settingIntent);
        }
        if (id == R.id.item4) {
            Intent settingIntent = new Intent(this, Catesetting4.class);
            startActivity(settingIntent);
        }
        if (id == R.id.item5) {
            Intent settingIntent = new Intent(this, Catesetting5.class);
            startActivity(settingIntent);
        }

        return super.onOptionsItemSelected(item);
    }


}