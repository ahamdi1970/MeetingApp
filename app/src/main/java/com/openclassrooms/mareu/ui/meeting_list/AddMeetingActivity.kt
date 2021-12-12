package com.openclassrooms.mareu.ui.meeting_list

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.Color
import com.openclassrooms.mareu.service.MeetingApiService
import android.os.Bundle
import com.openclassrooms.mareu.di.DI
import com.openclassrooms.mareu.R
import android.text.TextWatcher
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.mareu.data.MeetingDB
import com.openclassrooms.mareu.data.MeetingViewModel
import com.openclassrooms.mareu.databinding.ActivityAddMeetingBinding
import com.openclassrooms.mareu.model.Meeting
import java.util.*

class AddMeetingActivity : AppCompatActivity(), OnDateSetListener, OnTimeSetListener, AdapterView.OnItemSelectedListener {

    private var mApiService: MeetingApiService? = null
    private var binding: ActivityAddMeetingBinding? = null
    private lateinit var mMeetingViewModel: MeetingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApiService = DI.getMeetingApiService()
        mMeetingViewModel = ViewModelProvider(this).get(MeetingViewModel::class.java)
        initView()
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        initSpinnerRoom()
        initListener()
        setMeeting()
    }

    private fun initView() {
        binding = ActivityAddMeetingBinding.inflate(this.layoutInflater)
        val view: View = binding!!.root
        setContentView(view)
    }

    private val randomColor: Int
        private get() {
            val random = Random()
            return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        }

    private fun initSpinnerRoom() {
        //read spinner array
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.rooms, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.spinner1.adapter = adapter
        binding!!.spinner1.onItemSelectedListener = this
    }

    private fun initListener() {
        //read date selected
        binding!!.buttonDatePicker.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        }
        //read hour selected
        binding!!.buttonHourPicker.setOnClickListener { v: View? ->
            val timePicker: DialogFragment = TimePickerFragment()
            timePicker.show(supportFragmentManager, "time picker")
        }
        //save the new meeting
        binding!!.createButton.setOnClickListener { v: View? -> addMeeting() }
    }

    private fun setMeeting() {
        binding!!.nomReunion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding!!.createButton.isEnabled = s.length > 0
            }
        })
    }

    private fun addMeeting() {
        val meeting = MeetingDB(
                System.currentTimeMillis(),
                binding!!.nomReunion.text.toString(),
                binding!!.etDateReunion.text.toString(), binding!!.etHourReunion.text.toString(),
                binding!!.etRoom.text.toString(),
                binding!!.etMailInvites.text.toString(), randomColor
        )
        //mApiService!!.createMeeting(meeting)
        insertDataToDatabase()
        Log.d("tagii", "meeting = $meeting")
        finish()
    }

    private fun insertDataToDatabase() {
        val meetingName = binding!!.nomReunion.text.toString()
        val meetingDate = binding!!.etDateReunion.text.toString()
        val meetingHour = binding!!.etHourReunion.text.toString()
        val meetingRoom = binding!!.etRoom.text.toString()
        val meetingEmails = binding!!.etMailInvites.text.toString()
        val meetingColor = randomColor

        if (inputCheck(meetingName, meetingDate, meetingHour, meetingRoom, meetingEmails, meetingColor)) {
            // create Meeting Object
            val meeting = MeetingDB(0, meetingName, meetingDate, meetingHour, meetingRoom, meetingEmails, meetingColor)
            // Add Data to Database
            mMeetingViewModel.addMeeting(meeting)
            Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
            // Navigate Back
            val intent = Intent(applicationContext, ListMeetingActivity::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Please feel out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(meetingName: String, meetingDate: String,
                           meetingHour: String, meetingRoom: String, meetingEmails: String,
                           meetingColor: Int): Boolean {
        return !(TextUtils.isEmpty(meetingName) && TextUtils.isEmpty(meetingDate) && TextUtils.isEmpty(meetingHour) && TextUtils.isEmpty(meetingRoom) && TextUtils.isEmpty(meetingEmails) && TextUtils.isEmpty(meetingColor.toString()))
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val dayOfMonthStr = dayOfMonth.toString()
        val monthStr = (month + 1).toString()
        val yearStr = year.toString()
        val currentDateString = getString(R.string.date_meeting, dayOfMonthStr, monthStr, yearStr)
        binding!!.etDateReunion.setText(currentDateString)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val timeString = "$hourOfDay:$minute" // time formatting
        binding!!.etHourReunion.setText(timeString)
    }

    //to be able to go back to main activity with screen left arrow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // to be able to use spinner
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val text = parent.getItemAtPosition(position).toString()
        binding!!.etRoom.setText(text)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}