package com.AppProject.audiorecipe.MenualFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.AppProject.audiorecipe.R;

import androidx.fragment.app.Fragment;

public class helpMenual extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.helpmenual, container, false);

        return rootView;
    }

}
