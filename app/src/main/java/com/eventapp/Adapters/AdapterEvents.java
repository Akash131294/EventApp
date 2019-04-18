package com.eventapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventapp.Home;
import com.eventapp.Model.Event;
import com.eventapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterEvents extends RecyclerView.Adapter<AdapterEvents.Holder> {

    ArrayList<Event> events;
    Context context;
    int res;

    public AdapterEvents(ArrayList<Event> e, Context c, int r){
        this.context = c;
        this.events = e;
        this.res = r;
    }

    @NonNull
    @Override
    public AdapterEvents.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(context).inflate(res,viewGroup,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEvents.Holder holder, final int i) {
        Event event = events.get(i);
        holder.textViewName.setText(event.getName());
        holder.textViewCreatorName.setText(event.getCreatorName());
        ArrayList<String> participants = event.getParticipants();
        String val="";
        if (participants!=null && participants.size()>0){
            for (int j=0;j<participants.size();j++){
                if (j!=participants.size()-1)
                    val = val.concat(participants.get(j)+", ");
                else
                    val = val.concat(participants.get(j));
            }
        }else {
            val = "-";
        }
        holder.textViewParticipant.setText(val);
        holder.textViewDate.setText(event.getDate());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(i);
            }
        });

    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder alertCheck = new AlertDialog.Builder(context);

        alertCheck.setTitle("Delete Event");
        alertCheck.setMessage("Are you sure to remove this event?");

        alertCheck.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    events.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                ((Home)context).database.update(events);
            }
        });
        alertCheck.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });

        AlertDialog alertDialog = alertCheck.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCreatorName, textViewParticipant, textViewDate;
        ImageView imageViewDelete;
        public Holder(@NonNull View itemView) {
            super(itemView);

            textViewCreatorName = itemView.findViewById(R.id.tvCreatorValues);
            textViewName= itemView.findViewById(R.id.tvNameValues);
            textViewParticipant= itemView.findViewById(R.id.tvParticipantsValues);
            textViewDate= itemView.findViewById(R.id.tvDateValues);
            imageViewDelete = itemView.findViewById(R.id.imgDeleteEvent);
        }
    }

    public void addItem(Event e, int pos){
        this.events.add(0,e);
        notifyItemInserted(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
