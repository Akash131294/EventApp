package com.eventapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eventapp.Model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private DatabaseReference reference;
    private DatabaseListener mListener;
    public Database(Context context){
        mListener = (DatabaseListener) context;
        reference  = FirebaseDatabase.getInstance().getReference();
        reference.child("events").orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListener.onDataReceive(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface DatabaseListener{
        void onDataReceive(DataSnapshot dataSnapshot);
    }

    public void update(ArrayList<Event> val){
        ArrayList<HashMap<String,Object>> updateValues = new ArrayList<>();
        for (Event e : val){
            HashMap<String,Object> values = new HashMap<>();
            values.put("eName", e.getName());
            values.put("eCreatorName", e.getCreatorName());
            values.put("ePart", e.getParticipants());
            values.put("timestamp", e.getTimeStamp());
            updateValues.add(values);
        }
        HashMap<String,Object> fin = new HashMap<>();
        fin.put("events",updateValues);
        reference.updateChildren(fin);
    }

}
