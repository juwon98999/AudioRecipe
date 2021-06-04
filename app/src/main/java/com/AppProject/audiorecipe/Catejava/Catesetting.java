package com.AppProject.audiorecipe.Catejava;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.AppProject.audiorecipe.R;
import com.AppProject.audiorecipe.Sign1;

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


        getSupportActionBar().setTitle("카테고리 메뉴 (찌개)");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFD700));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //액션바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();


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