package com.nnnn.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageView left_arrow, right_arrow, pause_btn, play_btn;
    SeekBar seekBar;
    TextView time, song_name;
    MediaPlayer mediaPlayer;

    Handler handler = new Handler();
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        left_arrow = findViewById(R.id.left_arrow);
        right_arrow = findViewById(R.id.right_arrow);
        pause_btn = findViewById(R.id.pause_btn);
        play_btn = findViewById(R.id.play_btn);
        seekBar = findViewById(R.id.seekBar);
        time = findViewById(R.id.time);
        song_name = findViewById(R.id.song_name);


        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        song_name.setText(getResources().getIdentifier(
                "song", "raw", getPackageName()
        ));
        seekBar.setClickable(false);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic();
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo(((int) startTime));
                } else {
                    Toast.makeText(MainActivity.this, "Song Finished", Toast.LENGTH_SHORT).show();
                }
            }
        });

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't Go Back", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PlayMusic() {

        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();


        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        time.setText(String.format("%d min, %d sec",

                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds((TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);


    }


    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MINUTES.toMinutes((long) startTime)-TimeUnit.MILLISECONDS.toSeconds((long) startTime)
            )
            ));


            seekBar.setProgress((int) startTime);
            handler.postDelayed(this, 100);


        }
    };
}