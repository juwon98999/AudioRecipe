package com.example.audiorecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class RecipeMusic extends AppCompatActivity {

    MediaPlayer mp;
    int pos;
    private Button bStart;
    private Button bPause;
    SeekBar sb; // 음악 재생위치를 나타내는 시크바
    boolean isPlaying = false; // 재생중인지 확인할 변수

    TextView text1;


    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    Context cThis;
    String LogTT = "[STT]";

    Intent sttIntent;
    SpeechRecognizer mRecognizer;       //음성인식

    TextToSpeech tts;

    Button btnSttStart;
    EditText txtInMsg;   //음성인식 서버 메세지창

    ScrollView scrollView;
    BitmapDrawable bitmap;


    class MyThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while(isPlaying) {
                sb.setProgress(mp.getCurrentPosition());
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipetest);

        scrollView = (ScrollView)findViewById(R.id.scroll);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        scrollView.setHorizontalScrollBarEnabled(true);

        Resources res = getResources();
        bitmap = (BitmapDrawable) res.getDrawable((R.drawable.testimg));
        int bitmapWidth = bitmap.getIntrinsicWidth();
        int bitmapHeight = bitmap.getIntrinsicHeight();

        imageView.setImageDrawable(bitmap);
        imageView.getLayoutParams().width = bitmapWidth;
        imageView.getLayoutParams().height = bitmapHeight;

        cThis = this;

        mp = MediaPlayer.create(RecipeMusic.this, R.raw.testsound);
        bStart = (Button)findViewById(R.id.Start);
        bPause = (Button)findViewById(R.id.pause);
        text1 = (TextView)findViewById(R.id.time);



        sb = (SeekBar)findViewById(R.id.sebar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                int ttt = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                mp.seekTo(ttt);
                mp.start();
                new MyThread().start();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                mp.pause();
            }
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {

                if(fromUser) {
                    mp.seekTo(progress);
                }

                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d", m, s);
                text1.setText(strTime);

                if (seekBar.getMax()==progress) {
                    isPlaying = false;
                    mp.stop();
                }
            }
        });


        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.setLooping(false); // true:무한반복
                mp.start(); // 노래 재생 시작

                int a = mp.getDuration(); // 노래의 재생시간(miliSecond)
                sb.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
                new MyThread().start(); // 씨크바 그려줄 쓰레드 시작
                isPlaying = true; // 씨크바 쓰레드 반복 하도록


            }
        });

        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일시중지
                pos = mp.getCurrentPosition();
                mp.pause(); // 일시중지
                isPlaying = false; // 쓰레드 정지
            }
        });



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

        btnSttStart = (Button) findViewById(R.id.ttsbtn);
        btnSttStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("-------------------------------------- 음성인식 시작!");
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RecipeMusic.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
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

        txtInMsg = (EditText) findViewById(R.id.txtMsg);


        //5초마다 반복실행
        final Handler handler = new Handler(){
            public  void  handleMessage(Message msg){
                btnSttStart.performClick();
                btnSttStart.setVisibility(View.INVISIBLE);
            }
        };
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
            @Override
            public boolean cancel() {return super.cancel();}
        };
        timer.schedule(timerTask, 0 , 5000);

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
            txtInMsg.setText( rs[0] + "\r\n" + txtInMsg.getText() );
            FuncVoicdOrderCheck(rs[0]);

            mRecognizer.startListening(sttIntent); //음성인식이 계속 되는 구문이니 필요에 맞게 쓰시길 바람
        }
    };

    private void FuncVoicdOrderCheck(String VoiceMsg){
        if(VoiceMsg.length() < 1) return;

        VoiceMsg = VoiceMsg.replace(" ","");

        if(VoiceMsg.indexOf("재생해줘") > -1 || VoiceMsg.indexOf("재생") > -1){
            mp.setLooping(false); // true:무한반복
            mp.start(); // 노래 재생 시작

            int a = mp.getDuration(); // 노래의 재생시간(miliSecond)
            sb.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
            new MyThread().start(); // 씨크바 그려줄 쓰레드 시작
            isPlaying = true; // 씨크바 쓰레드 반복 하도록
            FuncVoiceOut("재생 되었습니다.");  //재생시 연속으로 여러개가 재생되는현상 수정해야함 현재는 음성인식시 자동종료되는 오류발생
        }

        if(VoiceMsg.indexOf("정지해줘") > -1 || VoiceMsg.indexOf("일시정지") > -1){
            Log.i(LogTT, "메세지 확인 : 일시정지");
            pos = mp.getCurrentPosition();
            mp.pause(); // 일시중지
            isPlaying = false; // 쓰레드 정지
            FuncVoiceOut("일시정지 되었습니다.");
        }


    }

    private void FuncVoiceOut(String OutMsg){
        if(OutMsg.length() < 1) return;

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


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더누르면 메인화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPlaying = false; // 쓰레드 정지
        if (mp!=null) {
            mp.release(); // 자원해제
        }
    }


}