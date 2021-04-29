package com.example.studytimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studytimer.ui.preferencescreen.PreferenceScreenFragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TimeAdp adapter;
    private TextView showtime;
    private TextView perday;
    private TextView mtitle;
    private Button mstart;
    private Timer mtimer;
    private TimerTask mtimerTask;
    private Double mtime = 0.0;
    private boolean isstart = false;
    //private Button gotohistory;
    private List<TimerModel> timelist;
    private Double ttime = 0.0;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showtime = findViewById(R.id.uoaclock);
        perday = findViewById(R.id.perday);
        mstart = findViewById(R.id.uoastart);
        mtitle = findViewById(R.id.uoatitle);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = preferences.getString("prefer_name", " null");
        mtitle.setText("Hi " + value + ", you have studied: ");
        mtimer = new Timer();

/*        gotohistory = findViewById(R.id.uoahistory);
        Onclick onclick = new Onclick();
        gotohistory.setOnClickListener(onclick);*/
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
        Intent intent;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent = new Intent(MainActivity.this, PreferenceScreen.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_history) {
            intent = new Intent(MainActivity.this, History.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {

        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = preferences.getString("prefer_name", " null");
        mtitle.setText("Hi " + value + ", you have studied: ");

    }

    public void onPause() {

        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("prefer_focus", false) && isstart){
            isstart = false;
            mstart.setText("CONTINUE");
            mstart.setTextColor(ContextCompat.getColor(this, R.color.green));
            mtimerTask.cancel();
        }
    }
    /*class Onclick implements View.OnClickListener{
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
    }*/
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
        final AlertDialog dialog = newdayAlert.create();
        View dialogView = View.inflate(this, R.layout.save_slot_dialog, null);
        dialog.setView(dialogView);
        dialog.show();
        final EditText etName = dialogView.findViewById(R.id.et_name);
        Button btnLogin =dialogView.findViewById(R.id.btn_save);
        Button btnCancel =dialogView.findViewById(R.id.btn_cancel);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slotName = etName.getText().toString();
                if (TextUtils.isEmpty(slotName)) {
                    Toast.makeText(MainActivity.this, "Please enter your slot name", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(mtimerTask != null)
                    {
                        addhistory();
                        ttime += mtime;
                        if (ttime>=60) perday.setText(getwordtime(ttime));
                        mtimerTask.cancel();
                        mstart.setText("START");
                        mstart.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                        mtime = 0.0;
                        isstart = false;
                        showtime.setText("00 : 00 : 00");
                        dialog.dismiss();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /*AlertDialog.Builder newdayAlert = new AlertDialog.Builder(this);
        newdayAlert.setTitle("Save your slot");
        newdayAlert.setMessage("Are you sure you want to stop slot?\nToday's study time will be recorded.");
        newdayAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
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
        newdayAlert.show();*/
    }

    private void addhistory() {
        Toast.makeText(MainActivity.this,getshowtime(mtime)+" has been added to history",Toast.LENGTH_SHORT).show();
        TimerModel timerModel = new TimerModel(getshowtime(mtime),mtime);
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
                        showtime.setText(getshowtime(mtime));
                    }
                });
            }
        };
        mtimer.scheduleAtFixedRate(mtimerTask, 0 ,1000);
    }
    
    private String getshowtime(double time)
    {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);
        String output = String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
        return output;
    }
    private String getwordtime(double time)
    {
        int rounded = (int) Math.round(time);
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);
        String h = " Hour ";
        if (hours > 1) h = " Hours ";
        String m = " Minute ";
        if (minutes > 1) m = " Minutes ";
        String output = hours + h + minutes + m;
        return output;
    }

}