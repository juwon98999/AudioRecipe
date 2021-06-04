package com.AppProject.audiorecipe.Catejava;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.AppProject.audiorecipe.R;
import com.AppProject.audiorecipe.Sign3;

import androidx.appcompat.app.AppCompatActivity;

public class Catesetting3 extends AppCompatActivity {

    ImageButton recipebtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category3);

        recipebtn3 = (ImageButton) findViewById(R.id.menu3);

        recipebtn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Sign3.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("카테고리 메뉴 (고기)"); //액션바 타이틀 변경
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
            Intent settingIntent = new Intent(this, Catesetting2.class);
            startActivity(settingIntent);
        }
        if (id == R.id.item3) {

        }

        return super.onOptionsItemSelected(item);
    }


}
