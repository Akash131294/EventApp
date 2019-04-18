package com.eventapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.eventapp.Adapters.AdapterEvents;
import com.eventapp.Model.Event;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Home extends AppCompatActivity implements Database.DatabaseListener , View.OnClickListener,
        AddEventDialog.EventDialogListener {
    private RecyclerView recyclerView;
    ImageView imageViewAdd, imageViewClose;
    public Database database;
    private static String TAG = "HOME.JAVA";
    ArrayList<Event> events = new ArrayList<>();
    Context context;
    AdapterEvents adapterEvents;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initializeViews();
        progressBar.setVisibility(View.VISIBLE);

        database = new Database(this);
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressHome);
        recyclerView = findViewById(R.id.recycleEvents);
        imageViewAdd = findViewById(R.id.imgAdd);
        imageViewClose = findViewById(R.id.imgClose);

        imageViewClose.setOnClickListener(this);
        imageViewAdd.setOnClickListener(this);
    }

    @Override
    public void onDataReceive(DataSnapshot dataSnapshot) {
        Log.d(TAG,"Data Receive: "+dataSnapshot);
        if (dataSnapshot.hasChildren()){
            events = new ArrayList<>();
            ArrayList<HashMap<String,Object>> val= (ArrayList<HashMap<String, Object>>) dataSnapshot.getValue();
            if (val== null) return;
            for (int i =0 ; i< val.size();i++){
                HashMap<String,Object> values = val.get(i);
                if (values!=null && values.containsKey("eName") && values.containsKey("eCreatorName") && values.containsKey("timestamp")
                        && values.containsKey("ePart")){
                    Event event = new Event(values.get("eName").toString(),values.get("eCreatorName").toString(),
                            (ArrayList<String>) values.get("ePart"),(long)values.get("timestamp"));
                    events.add(event);
                }
            }
            adapterEvents = new AdapterEvents(events, context, R.layout.recycle_events);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapterEvents);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgAdd:
                new AddEventDialog(context);
                break;

            case R.id.imgClose:
                finish();
                break;
        }
    }

    @Override
    public void eventDialogCancelled() {
        Log.d(TAG,"Event Dialog Cancelled: ");

    }

    @Override
    public void eventCreated(Event event) {
        Log.d(TAG,"Event Created: "+event.getName());
        events.add(0,event);
        database.update(events);
        if (adapterEvents == null){
            adapterEvents = new AdapterEvents(events, context, R.layout.recycle_events);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapterEvents);
        }else{
            adapterEvents.notifyItemInserted(0);
        }
    }
}
