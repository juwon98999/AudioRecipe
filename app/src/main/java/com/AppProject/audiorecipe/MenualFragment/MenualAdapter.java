package com.AppProject.audiorecipe.MenualFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MenualAdapter extends FragmentStateAdapter {
    public int mCount;

    public MenualAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if (index == 0) return new helpMenual();
        else if (index == 1) return new Menual1();
        else if (index == 2) return new Menual2();
        else if (index == 3) return new Menual3();
        else if (index == 4) return new Menual4();
        else if (index == 5) return new Menual5();
        else if (index == 6) return new Menual6();
        else if (index == 7) return new Menual7();
        else if (index == 8) return new Menual8();
        else if (index == 9) return new Menual9();
        else return new Menual10();
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    public int getRealPosition(int position) { return position % mCount; }

}