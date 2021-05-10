package com.example.audiorecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PopupClass2 extends Activity {

    TextView viewText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupmain2);


        viewText = (TextView)findViewById(R.id.popupview);
        viewText.setText("국수를 후레이크스프와 함께 끓는 물에 넣어서 요리하는 국수 형태의 인스턴트 식품이다.");
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
