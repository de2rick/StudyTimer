package com.example.studytimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView showtime;
    private Button mstart;
    private Timer mtimer;
    private TimerTask mtimerTask;
    private Double mtime = 0.0;
    private boolean isstart = false;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showtime = findViewById(R.id.uoaclock);
        mstart = findViewById(R.id.uoastart);
        mtimer = new Timer();
    }

    public void studyreset(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(mtimerTask != null)
                {
                    mtimerTask.cancel();
                    mstart.setText("START");
                    mstart.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                    mtime = 0.0;
                    isstart = false;
                    showtime.setText("00 : 00 : 00");
                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });
        resetAlert.show();
    }

    public void setnewday(View view) {
        AlertDialog.Builder newdayAlert = new AlertDialog.Builder(this);
        newdayAlert.setTitle("Set a new day");
        newdayAlert.setMessage("Are you sure you want to set a new day?\n Today’s study time will be recorded.");
        newdayAlert.setPositiveButton("New Day", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(mtimerTask != null)
                {
                    mtimerTask.cancel();
                    mstart.setText("START");
                    mstart.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                    mtime = 0.0;
                    isstart = false;
                    showtime.setText("00 : 00 : 00");
                }
            }
        });

        newdayAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });
        newdayAlert.show();
    }

    public void studystart(View view)
    {
        if(isstart)
        {
            isstart = false;
            mstart.setText("START");
            mstart.setTextColor(ContextCompat.getColor(this, R.color.green));
            mtimerTask.cancel();
        }
        else
        {
            isstart = true;
            mstart.setText("STOP");
            mstart.setTextColor(ContextCompat.getColor(this, R.color.red));
            startTimer();
        }
    }

    private void startTimer()
    {
        mtimerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mtime++;
                        showtime.setText(getshowtime());
                    }
                });
            }
        };
        mtimer.scheduleAtFixedRate(mtimerTask, 0 ,1000);
    }
    
    private String getshowtime()
    {
        int rounded = (int) Math.round(mtime);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);
        String output = String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
        return output;
    }


}