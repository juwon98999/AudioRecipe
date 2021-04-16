package com.example.audiorecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import me.relex.circleindicator.CircleIndicator3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiorecipe.Catejava.Catesetting;
import com.example.audiorecipe.Fragment.MyAdapter;

public class MainActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    private Button recipe; //레시피
    private Button cate; //카테고리

    private ViewPager2 mPager;      //이미지 슬라이드 관련 선언
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;

    ScrollView mainscroll;

    Button popup1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        popup1 = (Button) findViewById(R.id.popupbtn1);

        popup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PopupClass.class);
                intent.putExtra("data", "요리재료 소개내용입니다.");
                startActivityForResult(intent, 1);

            }
        });



        //이미지 슬라이드 관련
        mPager = findViewById(R.id.viewpager);  //ViewPager2

        pagerAdapter = new MyAdapter(this, num_page); //Adapter
        mPager.setAdapter(pagerAdapter);

        mIndicator = findViewById(R.id.indicator);  //Indicator
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);

        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);  //ViewPager setting
        mPager.setCurrentItem(1000);
        mPager.setOffscreenPageLimit(3);



        //mainscroll = (ScrollView)findViewById(R.id.mainsc);
        //mainscroll.setHorizontalScrollBarEnabled(true);


        recipe = (Button) findViewById(R.id.recipepage1);
        cate = (Button) findViewById(R.id.categorypage);

        recipe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipeMusic.class);
                startActivity(intent);
            }
        });

        cate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Catesetting.class);
                startActivity(intent);
            }
        });

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }

        });


        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);



        //이미지 슬라이드 관련
        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });
    }



            //뒤로가기 버튼 관련
        @Override
        public void onBackPressed() {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더누르면 어플을 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }
