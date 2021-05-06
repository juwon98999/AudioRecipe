package com.example.audiorecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import me.relex.circleindicator.CircleIndicator3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.audiorecipe.Catejava.Catesetting;
import com.example.audiorecipe.Fragment.MyAdapter;
import com.example.audiorecipe.Recipe.RecipeDetail;
import com.example.audiorecipe.Recipe.RecipeMusic;

import java.util.ArrayList;
import java.util.Locale;

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

    Button helpbtn;


    //음성인식
    Context cThis;
    String LogTT = "[STT]";
    Intent sttIntent;
    SpeechRecognizer mRecognizer;
    TextToSpeech tts;
    Button ttsBtn;      //tts 버튼 숨기기
    EditText txtInMsg;   //음성인식 메세지창


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar(); //액션바 숨기기
        actionBar.hide();

        helpbtn = (Button) findViewById(R.id.helpPage); //도움말로 화면전환
        helpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Help.class);
                startActivity(intent);
            }
        });


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



        mainscroll = (ScrollView)findViewById(R.id.mainsc);
        mainscroll.setHorizontalScrollBarEnabled(true);

        mainscroll = (ScrollView)findViewById(R.id.mainsc);
        mainscroll.setHorizontalScrollBarEnabled(true);


        recipe = (Button) findViewById(R.id.recipepage1);
        cate = (Button) findViewById(R.id.categorypage);

        recipe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sign1.class);
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

        cThis = this;

        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getApplicationContext().getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis);
        mRecognizer.setRecognitionListener(listener);


        tts = new TextToSpeech(cThis, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != android.speech.tts.TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        ttsBtn = (Button) findViewById(R.id.mainttsbtn);
        ttsBtn.setVisibility(View.VISIBLE);
        txtInMsg = (EditText) findViewById(R.id.mainttstxt);
        ttsBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("-------------------------------------- 음성인식 시작!");
                txtInMsg.setVisibility(View.INVISIBLE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    //권한을 허용하지 않는 경우
                } else {
                    //권한을 허용한 경우
                    try {
                        mRecognizer.startListening(sttIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            System.out.println("onReadyForSpeech.........................");
        }

        @Override
        public void onBeginningOfSpeech() {
            System.out.println("onBeginningOfSpeech.........................");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            System.out.println("onRmsChanged.........................");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            System.out.println("onBufferReceived.........................");
        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("onEndOfSpeech.........................");
        }

        @Override
        public void onError(int error) {
            System.out.println("onError.........................");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            System.out.println("onPartialResults.........................");
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            System.out.println("onEvent.........................");
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            Log.i(LogTT, "입력값 : " + rs[0]);
            txtInMsg.setText(rs[0] + "\r\n" + txtInMsg.getText());
            FuncVoicdOrderCheck(rs[0]);

            mRecognizer.startListening(sttIntent); //음성인식이 계속 되는 구문이니 필요에 맞게 쓰시길 바람
        }
    };

    private void FuncVoicdOrderCheck(String VoiceMsg) {
        if (VoiceMsg.length() < 1) return;

        VoiceMsg = VoiceMsg.replace(" ", "");

        if (VoiceMsg.indexOf("재생해줘") > -1 || VoiceMsg.indexOf("재생") > -1) {
            Log.i(LogTT, "메세지 확인 : 재생"); //명령어 추가할것
            FuncVoiceOut("재생 되었습니다.");  //재생시 연속으로 여러개가 재생되는현상 수정해야함 현재는 음성인식시 자동종료되는 오류발생
        }

        if (VoiceMsg.indexOf("정지해줘") > -1 || VoiceMsg.indexOf("일시정지") > -1) {
            Log.i(LogTT, "메세지 확인 : 일시정지");
            FuncVoiceOut("일시정지 되었습니다.");
        }
    }

    private void FuncVoiceOut(String OutMsg) {
        if (OutMsg.length() < 1) return;

        tts.setPitch(1.5f);
        tts.setSpeechRate(1.0f);
        tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }

        if (mRecognizer != null) {
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer = null;
        }
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
