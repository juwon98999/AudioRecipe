package com.AppProject.audiorecipe.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.AppProject.audiorecipe.R;

import androidx.fragment.app.Fragment;

public class FragmentSecond extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_2p, container, false);

        return rootView;
    }

}
