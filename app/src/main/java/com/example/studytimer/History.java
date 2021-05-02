package com.example.studytimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private TimeAdp adapter;
    private List<TimerModel> timelist;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.recyclerview);

        timelist = savehistory.readListFromPref(this);

        if (timelist == null)
            timelist = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new TimeAdp(getApplicationContext(), timelist);
        recyclerView.setAdapter(adapter);

    }

    public void historyreset(View view)
    {
        timelist = new ArrayList<>();
        savehistory.writeListInPref(getApplicationContext(), timelist);
        //Collections.reverse(timelist);
        adapter.setTaskModelList(timelist);

    }
}
