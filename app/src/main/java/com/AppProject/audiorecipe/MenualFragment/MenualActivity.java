package com.AppProject.audiorecipe.MenualFragment;

import android.os.Bundle;

import com.AppProject.audiorecipe.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import me.relex.circleindicator.CircleIndicator3;

public class MenualActivity extends AppCompatActivity {

    private ViewPager2 mPager;
    private FragmentStateAdapter menualAdapter;
    private int num_page = 11;
    private CircleIndicator3 mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menualguide);
        ActionBar actionBar = getSupportActionBar(); //액션바 숨기기
        actionBar.hide();

        mPager = findViewById(R.id.viewpager);  //ViewPager2

        menualAdapter = new MenualAdapter(this, num_page); //Adapter
        mPager.setAdapter(menualAdapter);

        mIndicator = findViewById(R.id.indicator);  //Indicator
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);

        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(11);

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


    }


}
