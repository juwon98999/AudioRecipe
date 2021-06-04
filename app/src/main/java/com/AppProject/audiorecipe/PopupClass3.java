package com.AppProject.audiorecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PopupClass3 extends Activity {

    TextView viewText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupmain3);


        viewText = (TextView)findViewById(R.id.popupview);
        viewText.setText("고기에 김치 , 대파 등 여러 가지 야채를 넣고 국물이 조금 있는 상태에서 볶듯이 끓인 음식이다.");
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
