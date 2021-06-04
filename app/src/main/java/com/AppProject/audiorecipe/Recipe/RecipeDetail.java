package com.AppProject.audiorecipe.Recipe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.AppProject.audiorecipe.R;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RecipeDetail extends AppCompatActivity implements SensorEventListener {

    MediaPlayer mp;
    int pos;
    private ImageButton bStart;
    private ImageButton bPause;
    private ImageButton bplus; //5초후
    private ImageButton bminus; //5초전
    SeekBar sb; // 음악 재생위치를 나타내는 시크바
    boolean isPlaying = false; // 재생중인지 확인할 변수

    TextView text1;


    private final long FINISH_INTERVAL_TIME = 10000000;
    private long backPressedTime = 0;

    Context cThis;
    String LogTT = "[STT]";

    Intent sttIntent;
    SpeechRecognizer mRecognizer;       //음성인식

    TextToSpeech tts;

    Button ttsBtn;      //tts 버튼 숨기기
    EditText txtInMsg;   //음성인식 메세지창

    ScrollView scrollView;

    private SensorManager sensormanager;    //온도센서
    private Sensor sensorTemp;
    private boolean isTempratureSensorAvailble;
    TextView mtemp;
    private Sensor sensorLight; //조도 센서
    private boolean isLightAvailble;

    SoundPool sp; //효과음
    int fireSoundID;
    int alramSoundID;

    private static final long START_TIMT_IN_MILLIS = 600000; //시간초 입력
    private TextView counttext;
    private ImageButton countbtn;
    private CountDownTimer downTimer;
    private boolean timerRunning;
    private long timeinmillis = START_TIMT_IN_MILLIS;



    class DetailThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while (isPlaying) {
                sb.setProgress(mp.getCurrentPosition());
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipedetailtest);
        ActionBar actionBar = getSupportActionBar(); //액션바 숨기기
        actionBar.hide();


        //사운드 풀
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }else
        {
            SoundPool timesp = new SoundPool(1, AudioManager.STREAM_ALARM,
                    0);
        }
        fireSoundID = sp.load(this, R.raw.siren, 1);
        alramSoundID = sp.load(this, R.raw.timeralarm, 1);


        counttext = findViewById(R.id.conut);
        countbtn = (ImageButton) findViewById(R.id.timer1);

        countbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    Timerpause();
                    timerRunning = false;
                } else {
                    TimerStart();
                    timerRunning = true;
                }
            }
        });


        mtemp = (TextView) findViewById(R.id.temp); //주변온도 측정기
        sensormanager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorTemp = sensormanager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (sensormanager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            isTempratureSensorAvailble = true;
            //success
        } else {
            mtemp.setText("Sensor is not Availble");
            isTempratureSensorAvailble = false;
            //fail
        }
        sensorLight = sensormanager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensormanager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            isLightAvailble = true;
            //success
        } else {
            isLightAvailble = false;
            //fail
        }


        scrollView = (ScrollView) findViewById(R.id.scroll);
        scrollView.setHorizontalScrollBarEnabled(true);

        cThis = this;

        mp = MediaPlayer.create(RecipeDetail.this, R.raw.kimchiggigae2);
        bStart = (ImageButton) findViewById(R.id.start1);
        bPause = (ImageButton) findViewById(R.id.pause1);
        bplus = (ImageButton) findViewById(R.id.plusbtn1);
        bminus = (ImageButton) findViewById(R.id.minusbtn1);
        text1 = (TextView) findViewById(R.id.time);


        sb = (SeekBar) findViewById(R.id.sebar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                int ttt = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                mp.seekTo(ttt);
                mp.start();
                new DetailThread().start();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                mp.pause();
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mp.seekTo(progress);
                }

                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d", m, s);
                text1.setText(strTime);

                if (seekBar.getMax() == progress) {
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

                int pos = mp.getDuration(); // 노래의 재생시간(miliSecond)
                sb.setMax(pos);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
                new DetailThread().start(); // 씨크바 그려줄 쓰레드 시작
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

        bplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 5초후
                isPlaying = true;
                int pos = mp.getCurrentPosition();
                int duration = mp.getDuration();
                if(mp.isPlaying() && duration != pos){
                    pos = pos + 10000;
                    mp.seekTo(pos);
                }
            }
        });

        bminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 5초전
                isPlaying = true;
                int pos = mp.getCurrentPosition();
                int duration = mp.getDuration();
                if(mp.isPlaying() && duration != pos) {
                    pos = pos - 10000;
                    mp.seekTo(pos);
                }
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

        ttsBtn = (Button) findViewById(R.id.ttsbtn1);
        ttsBtn.setVisibility(View.INVISIBLE);
        txtInMsg = (EditText) findViewById(R.id.txtMsg);

    }


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            System.out.println("onReadyForSpeech.........................");
            Toast.makeText(getApplicationContext(), "음성인식 실행 되었습니다.", Toast.LENGTH_SHORT).show();
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

        }
    };

    private void FuncVoicdOrderCheck(String VoiceMsg) {
        if (VoiceMsg.length() < 1) return;

        VoiceMsg = VoiceMsg.replace(" ", "");

        if (VoiceMsg.indexOf("재생해줘") > -1 || VoiceMsg.indexOf("재생") > -1) {
            mp.setLooping(false); // true:무한반복
            mp.start(); // 노래 재생 시작

            int a = mp.getDuration(); // 노래의 재생시간
            sb.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
            new DetailThread().start(); // 씨크바 그려줄 쓰레드 시작
            isPlaying = true; // 씨크바 쓰레드 반복 하도록
        }

        if (VoiceMsg.indexOf("정지해줘") > -1 || VoiceMsg.indexOf("일시정지") > -1) {
            Log.i(LogTT, "메세지 확인 : 일시정지");
            pos = mp.getCurrentPosition();
            mp.pause(); // 일시중지
            isPlaying = false; // 쓰레드 정지
            FuncVoiceOut("일시정지 되었습니다.");
        }

        if (VoiceMsg.indexOf("앞으로") > -1 || VoiceMsg.indexOf("앞으로이동") > -1) {
            Log.i(LogTT, "메세지 확인 : 앞으로");
            isPlaying = true;
            int pos = mp.getCurrentPosition();
            int duration = mp.getDuration();
            if(mp.isPlaying() && duration != pos){
                pos = pos + 10000;
                mp.seekTo(pos);
            }
        }

        if (VoiceMsg.indexOf("뒤로") > -1 || VoiceMsg.indexOf("뒤로이동") > -1) {
            Log.i(LogTT, "메세지 확인 : 뒤로");
            isPlaying = true;
            int pos = mp.getCurrentPosition();
            int duration = mp.getDuration();
            if(mp.isPlaying() && duration != pos) {
                pos = pos - 10000;
                mp.seekTo(pos);
            }
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

        if(sensormanager != null){
            sensormanager.unregisterListener(this);
            sensormanager = null;
        }
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        timerRunning= false;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더누르면 메인화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            mtemp.setText("온도: " + event.values[0] + "℃");

            if (event.values[0] > 70.0f) {
                mtemp.setText("화재발생!!");
                sp.play(fireSoundID,1,1,1,-1,1);
            }
        }

        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            if(event.values[0] < 50.0f){
                System.out.println("-------------------------------------- 음성인식 시작!");
                txtInMsg.setVisibility(View.INVISIBLE);
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); //음성인식 사운드 제거
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true); //사운드 제거
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RecipeDetail.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
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
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isTempratureSensorAvailble) {
            sensormanager.registerListener(this, sensorTemp, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (isLightAvailble) {
            sensormanager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTempratureSensorAvailble) {
            sensormanager.unregisterListener(this);
        }
        isPlaying = false; // 쓰레드 정지
        if (mp != null) {
            mp.release(); // 자원해제
        }
    }


    private void TimerStart(){
        downTimer = new CountDownTimer(timeinmillis, 1000) {
            @Override
            public void onTick(long millisuntilFinsihed) {
                timeinmillis = millisuntilFinsihed;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                sp.play(alramSoundID,1,1,1,0,1);
            }
        }.start();
        timerRunning = true;
    }

    private  void Timerpause(){
        downTimer.cancel();
    }
    private void updateCountDownText(){
        int hours = (int) (timeinmillis/ (1000*60*60)) % 60;
        int minutes = (int) (timeinmillis / (1000*60)) % 60;
        int seconds = (int) (timeinmillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours,minutes, seconds);
        counttext.setText(timeFormatted);
    }


}

