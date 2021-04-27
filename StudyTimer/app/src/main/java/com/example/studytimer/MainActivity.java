package com.example.studytimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TimeAdp adapter;
    private TextView showtime;
    private Button mstart;
    private Timer mtimer;
    private TimerTask mtimerTask;
    private Double mtime = 0.0;
    private boolean isstart = false;
    private Button gotohistory;
    private List<TimerModel> timelist;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showtime = findViewById(R.id.uoaclock);
        mstart = findViewById(R.id.uoastart);
        mtimer = new Timer();
        gotohistory = findViewById(R.id.uoahistory);
        Onclick onclick = new Onclick();
        gotohistory.setOnClickListener(onclick);
        timelist = new ArrayList<>();
        adapter = new TimeAdp(getApplicationContext(), timelist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.uoahistory:
                    intent = new Intent(MainActivity.this, History.class);
                    break;
            }
            startActivity(intent);
        }
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
                    addhistory();
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

    private void addhistory() {
        Toast.makeText(MainActivity.this,getshowtime()+" has been added to history",Toast.LENGTH_SHORT).show();
        TimerModel timerModel = new TimerModel(getshowtime(),mtime);
        timelist.add(timerModel);
        savehistory.writeListInPref(getApplicationContext(), timelist);
        Collections.reverse(timelist);
        adapter.setTaskModelList(timelist);
    }

    public void studystart(View view)
    {
        if(isstart)
        {
            isstart = false;
            mstart.setText("CONTINUE");
            mstart.setTextColor(ContextCompat.getColor(this, R.color.green));
            mtimerTask.cancel();
        }
        else
        {
            isstart = true;
            mstart.setText("PAUSE");
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