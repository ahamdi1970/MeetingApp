package com.openclassrooms.mareu.ui.meeting_list;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.databinding.ActivityListMeetingBinding;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.events.DeleteMeetingEvent;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


public class ListMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityListMeetingBinding binding;

    private MeetingApiService apiService;

    private MyMeetingRecyclerViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = DI.getMeetingApiService();

        initView();

        setSupportActionBar(binding.meetingToolbar);

        initList(apiService.getMeetings ());

        binding.addMeeting.setOnClickListener( v -> {
            Intent intent = new Intent(getApplicationContext(), AddMeetingActivity.class);
            startActivity(intent);
        } );
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.sort_menu,menu );
        //menu.getItem ( 0 ).setIcon( AppCompatResources.getDrawable ( this,R.drawable.ic_sort_menu ));
        //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_sort_menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId ()){
            case R.id.sortbydate:
                DialogFragment datePicker = new DatePickerFragment ();
                datePicker.show ( getSupportFragmentManager (),"date picker" );
                return true;
            case R.id.sortbyroom:
                return true;
            case R.id.reset:
                initList (apiService.getMeetings ());
                return true;
            default:
                initList (apiService.returnMatchingMeetingsWithRoom ( item.getTitle ().toString ()));
                return true;
        }
    }

    private void initView() {
        binding = ActivityListMeetingBinding.inflate(this.getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Random random = new Random ();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }


    private void initList(List<Meeting> meetingList) {
        binding.listMeetings.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.listMeetings.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        adapter = new MyMeetingRecyclerViewAdapter ( meetingList );
        binding.listMeetings.setAdapter(adapter);
    }

    /**
     * Fired if the user clicks on a delete button
     *
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting (DeleteMeetingEvent event) {
        apiService.deleteMeeting(event.meeting);
        adapter.notifyDataSetChanged();
    }
    // to be able to use date selected

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dayOfMonthStr = String.valueOf (dayOfMonth);
        String monthStr = String.valueOf (month + 1); // index de table commence à 0 ?!
        String yearStr = String.valueOf (year);
        String currentDateString = getString (R.string.date_meeting,dayOfMonthStr,monthStr,yearStr);
        initList (apiService.returnMatchingMeetingsWithDate ( currentDateString ));
    }
}
