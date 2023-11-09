package com.example.audiodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay,btnForward,btnRewind,btnPause;
    private MediaPlayer mediaPlayer;

    private double startTime=0;
    private double finalTime=0;

    private Handler handler=new Handler();
    private int forwardTime=5000;
    private int backwardTime=5000;

    private SeekBar seekBar;
    private TextView txtTime,txtTimeLeft,txtSongName;

    private static int oneTimeOnly=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay=findViewById(R.id.btnPlay);
        btnForward=findViewById(R.id.btnForward);
        btnPause=findViewById(R.id.btnPause);
        btnRewind=findViewById(R.id.btnBack);

        txtTime=findViewById(R.id.songTime);
        txtSongName=findViewById(R.id.songName);
        txtTimeLeft=findViewById(R.id.timeLeft);

        txtSongName.setText("BDLP.mp4");

        mediaPlayer=MediaPlayer.create(this,R.raw.bdlp);
        seekBar=(SeekBar) findViewById(R.id.seekBar);
        seekBar.setClickable(true);
        btnPause.setEnabled(false);

        btnPlay.setOnClickListener(view -> {
            Toast.makeText(this, "Playing sound", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();

            finalTime=mediaPlayer.getDuration();
            startTime=mediaPlayer.getCurrentPosition();

            if(oneTimeOnly==0){
                seekBar.setMax((int)finalTime);
                oneTimeOnly=1;
            }

            txtTimeLeft.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
            );

            txtTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    startTime)))
            );;

            seekBar.setProgress((int)startTime);
            handler.postDelayed(UpdateSongTime, 100);
            btnPause.setEnabled(true);
            btnPlay.setEnabled(false);
        });

        btnPause.setOnClickListener(view -> {
            Toast.makeText(this, "Pausing sound", Toast.LENGTH_SHORT).show();
            mediaPlayer.pause();
            btnPause.setEnabled(false);
            btnPlay.setEnabled(true);
        });

        btnForward.setOnClickListener(view -> {
            int temp=(int) startTime;
            if((temp+forwardTime)<=finalTime){
                startTime=startTime+forwardTime;
                mediaPlayer.seekTo((int) startTime);
                Toast.makeText(this, "You have jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Cannot jump foward 5 seconds", Toast.LENGTH_SHORT).show();
            }
        });

        btnRewind.setOnClickListener(view -> {
            int temp = (int)startTime;

            if((temp-backwardTime)>0){
                startTime = startTime - backwardTime;
                mediaPlayer.seekTo((int) startTime);
                Toast.makeText(this, "You have jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            txtTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };
}