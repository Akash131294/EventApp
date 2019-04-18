package com.eventapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eventapp.Model.Event;
import com.eventapp.Utilities.HomeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddEventDialog implements View.OnClickListener{
    private Context context;
    private View view;
    private EditText editTextName, editTextCreator, editTextParticipants;
    private TextView textViewPickDate, textViewAddParticipants;
    private Button buttonSubmit;
    private RelativeLayout relativeLayoutParticipants;
    private boolean dateSelected = false;
    private ArrayList<TextView> textViewsParticipants = new ArrayList<>();
    private EventDialogListener listener;
    private ImageView imageViewClose;
    AlertDialog dialogAdd;
    public AddEventDialog(Context c){
        this.context = c;
        listener = (EventDialogListener) context;
        LayoutInflater layoutInflater = ((Home)context).getLayoutInflater();
        view  = layoutInflater.inflate(R.layout.add_event_dialog,null);
        initializeViews();

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        dialogAdd= dialogBuilder.create();
        dialogAdd.setView(view);

        dialogAdd.setCanceledOnTouchOutside(false);
        dialogAdd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.eventDialogCancelled();
            }
        });

        try {
            dialogAdd.show();
        }catch (Exception e){

        }
    }

    private void initializeViews() {
        imageViewClose = view.findViewById(R.id.imgCloseDialog);
        textViewAddParticipants= view.findViewById(R.id.tvAddParticipant);
        relativeLayoutParticipants = view.findViewById(R.id.relativeAddParticipant);
        editTextCreator = view.findViewById(R.id.edtCreatorName);
        editTextName= view.findViewById(R.id.edtEventName);
        editTextParticipants= view.findViewById(R.id.edtParticipants);
        textViewPickDate = view.findViewById(R.id.tvPickDate);
        buttonSubmit = view.findViewById(R.id.btnSubmit);

        buttonSubmit.setOnClickListener(this);
        textViewPickDate.setOnClickListener(this);
        textViewAddParticipants.setOnClickListener(this);
        imageViewClose.setOnClickListener(this);
    }

    Calendar date;
    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, final int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v("date_De", "The choosen one " + date.getTime());
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
                        Date d = date.getTime();
                        textViewPickDate.setText(sdf1.format(d)+" "+sdf2.format(d));
                        dateSelected = true;
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvPickDate:
                try {
                    InputMethodManager imm = (InputMethodManager)((Home)context).getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((Home)context).getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                showDateTimePicker();

                break;

            case R.id.btnSubmit:
                if (validateEntries()){
                    ArrayList<String> participants = new ArrayList<>();
                    String name = editTextName.getText().toString().trim();
                    String creatorName = editTextCreator.getText().toString().trim();
                    long time = date.getTimeInMillis();

                    if (textViewsParticipants!= null && textViewsParticipants.size()>0){
                        for (int i= 0;i<textViewsParticipants.size();i++){
                            participants.add(textViewsParticipants.get(i).getText().toString().trim());
                        }
                    }

                    Event event = new Event(name, creatorName, participants, time);
                    listener.eventCreated(event);
                    Toast.makeText(context, "Event Created", Toast.LENGTH_SHORT).show();
                    if (dialogAdd.isShowing())
                    dialogAdd.dismiss();
                }else if (!dateSelected){
                    Toast.makeText(context, "Event cannot be created without date and time.", Toast.LENGTH_LONG).show();
                }else if (textViewsParticipants.size() == 0){
                    android.support.v7.app.AlertDialog.Builder alertCheck = new android.support.v7.app.AlertDialog.Builder(context);
                    alertCheck.setTitle("Add Event");
                    alertCheck.setMessage("Are you sure want to add an Event without participants?");
                    alertCheck.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ArrayList<String> participants = new ArrayList<>();
                            String name = editTextName.getText().toString().trim();
                            String creatorName = editTextCreator.getText().toString().trim();
                            long time = date.getTimeInMillis();
                            Toast.makeText(context, "Event Created", Toast.LENGTH_LONG).show();
                            Event event = new Event(name, creatorName, participants, time);
                            listener.eventCreated(event);
                            if (dialogAdd.isShowing())
                                dialogAdd.dismiss();
                        }
                    });
                    alertCheck.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    android.support.v7.app.AlertDialog alertDialog = alertCheck.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(context, "All fields are required.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.imgCloseDialog:
                if (dialogAdd.isShowing()){
                    listener.eventDialogCancelled();
                    dialogAdd.dismiss();
                }
                break;

            case R.id.tvAddParticipant:
                if (editTextParticipants.getText().toString().trim().length()>0){
                    TextView textView = new TextView(context);
                    textView.setText(editTextParticipants.getText().toString().trim());
                    textView.setBackground(context.getDrawable(R.drawable.grey_corner_background));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.edtParticipants);
                    if (textViewsParticipants.size()>0)
                    params.addRule(RelativeLayout.RIGHT_OF,textViewsParticipants.get(textViewsParticipants.size()-1).getId());
                    params.setMargins(HomeUtils.getDpToPx(5,context),HomeUtils.getDpToPx(5,context),HomeUtils.getDpToPx(5,context),HomeUtils.getDpToPx(5,context));
                    textView.setLayoutParams(params);
                    textView.setId((textViewsParticipants.size()+1));
                    textView.setPadding(HomeUtils.getDpToPx(5,context),HomeUtils.getDpToPx(3,context),HomeUtils.getDpToPx(5,context),HomeUtils.getDpToPx(3,context));
                    textViewsParticipants.add(textView);
                    relativeLayoutParticipants.addView(textView);
                    editTextParticipants.getText().clear();
                }else {
                    Toast.makeText(context, "Enter a name to add as a participant.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean validateEntries() {
        if (editTextCreator.getText().toString().trim().length()>0 &&
                editTextName.getText().toString().trim().length()>0 &&
                (textViewsParticipants != null && textViewsParticipants.size()>0) && dateSelected){
            return true;
        }
        return false;
    }

    public interface EventDialogListener{
        void eventDialogCancelled();
        void eventCreated(Event event);
    }
}
