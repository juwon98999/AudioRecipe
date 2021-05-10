package com.example.audiorecipe.Catejava;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.audiorecipe.R;
import com.example.audiorecipe.Sign1;

import androidx.appcompat.app.AppCompatActivity;

public class Catesetting extends AppCompatActivity {

    ImageButton menurecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category1);

        menurecipe = (ImageButton) findViewById(R.id.menu1);

        menurecipe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sign1.class);
                startActivity(intent);
            }
        });


        getSupportActionBar().setTitle("카테고리 메뉴 (찌개)"); //액션바 타이틀 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFD700)); //액션바 배경색 변경
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //홈버튼 표시
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

        return super.onOptionsItemSelected(item);
    }


}