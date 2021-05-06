package com.example.audiorecipe.Catejava;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.audiorecipe.R;

import androidx.appcompat.app.AppCompatActivity;

public class Catesetting2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category2);

        getSupportActionBar().setTitle("카테고리 메뉴 (찌개)"); //액션바 타이틀 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFD700)); //액션바 배경색 변경
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //홈버튼 표시
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
            Intent settingIntent = new Intent(this, Catesetting.class);
            startActivity(settingIntent);
        }
        if (id == R.id.item2) {

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