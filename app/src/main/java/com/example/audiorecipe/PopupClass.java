package com.example.audiorecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PopupClass extends Activity {
    TextView viewText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupmain);


        viewText = (TextView)findViewById(R.id.popupview);
        viewText.setText("대표적인 한국 요리 중 하나로, 김치를 넣고 얼큰하게 끓인 찌개이다.");
    }


    public void mOnClose(View v){

        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);


        finish(); //팝업 닫기
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        return;
    }
}

