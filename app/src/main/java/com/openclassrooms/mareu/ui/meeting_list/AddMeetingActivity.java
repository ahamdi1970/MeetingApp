package com.openclassrooms.mareu.ui.meeting_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.databinding.ActivityAddMeetingBinding;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.MeetingApiService;

import java.util.Objects;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


public class AddMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {

    private MeetingApiService mApiService;

    private ActivityAddMeetingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApiService = DI.getMeetingApiService ();

        initView();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initSpinnerRoom();

        initListener();

        setMeeting();

    }

    private void initView() {
        binding = ActivityAddMeetingBinding.inflate(this.getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private int getRandomColor(){
        Random random = new Random ();
        return Color.argb (255,random.nextInt (256),random.nextInt (256),random.nextInt (256));
    }

    private void initSpinnerRoom(){
        //read spinner array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.rooms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner1.setAdapter(adapter);
        binding.spinner1.setOnItemSelectedListener( this );
    }

    private void initListener(){
        //read date selected
        binding.buttonDatePicker.setOnClickListener ( v -> {
            DialogFragment datePicker = new DatePickerFragment ();
            datePicker.show ( getSupportFragmentManager (),"date picker" );
        } );
        //read hour selected
        binding.buttonHourPicker.setOnClickListener ( v -> {
            DialogFragment timePicker = new TimePickerFragment ();
            timePicker.show(getSupportFragmentManager (),"time picker");
        } );
        //save the new meeting
        binding.createButton.setOnClickListener(v -> addMeeting());
    }

    private void setMeeting() {

        binding.nomReunion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.createButton.setEnabled(s.length() > 0);
            }
        });
    }

    private void addMeeting() {
        Meeting meeting = new Meeting(
                System.currentTimeMillis(),
                binding.nomReunion.getText().toString(),
                binding.etDateReunion.getText ().toString (), binding.etHourReunion.getText().toString(),
                binding.etRoom.getText().toString(),
                binding.etMailInvites.getText().toString(),getRandomColor ()
        );
        mApiService.createMeeting(meeting);
        Log.d("tagii","meeting = "+meeting);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year , int month ,int dayOfMonth ) {
        String dayOfMonthStr = String.valueOf (dayOfMonth);
        String monthStr = String.valueOf (month + 1);
        String yearStr = String.valueOf (year);
        String currentDateString = getString (R.string.date_meeting,dayOfMonthStr,monthStr,yearStr);
        binding.etDateReunion.setText ( currentDateString );
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeString =  hourOfDay +":"+ minute; // time formatting
        binding.etHourReunion.setText(timeString);

    }

    //to be able to go back to main activity with screen left arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


// to be able to use spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition ( position ).toString ();
        binding.etRoom.setText ( text );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }










}
