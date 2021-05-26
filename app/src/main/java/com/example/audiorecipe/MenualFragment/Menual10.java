package com.example.audiorecipe.MenualFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.audiorecipe.MainActivity;
import com.example.audiorecipe.R;

import androidx.fragment.app.Fragment;

public class Menual10 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.menual_10p, container, false);



        ImageButton imgbtn = (ImageButton) rootView.findViewById(R.id.homebtn);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        return rootView;

    }

}
