package com.example.studysupportapplication;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentAddSchedule extends Fragment {
    Button submitBtn;
    TextView startDateInputView, startTimeInputView, endDateInputView, endTimeInputView, startTimeView, endTimeView, startDateView, endDateView;
    EditText eventEditText;
    AutoCompleteTextView venueEditText;
    Switch isRepeatSwitch;
    String event;
    String venue;
    int maxID;
    static public String notificationKey="NOTIFICATION",notificationContentKey="NOTIFICATION_CONTENT"
            ,notificationIdKey="NOTIFICATION_CONTENT",notificationTagKey="NOTIFICATION_TAG"
            ,venueNameKey="VENUE_NAME";
    Date startDate, endDate=null, temp=null ;
    //Date startTime, endTime;
    boolean isRepeat=false;
    ScheduleViewModel scheduleViewModel;
    VenueViewModel venueViewModel;
    DatePickerDialog.OnDateSetListener listener, listener1;
    TimePickerDialog.OnTimeSetListener timeListener, timeListener1;
    List<String> venueName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_addschedule,container, false);
        submitBtn=view.findViewById(R.id.submitButton);
        startDateView=view.findViewById(R.id.startDateTextView);
        startTimeView=view.findViewById(R.id.startTimeTextView);
        endDateView=view.findViewById(R.id.endDateTextView);
        endTimeView=view.findViewById(R.id.endTimeTextView);
        isRepeatSwitch=view.findViewById(R.id.repeatSwitch);
        eventEditText=view.findViewById(R.id.eventEditText);
        venueEditText=view.findViewById(R.id.venueEditText);
        endDateView.setVisibility(View.INVISIBLE);
        endTimeView.setVisibility(View.INVISIBLE);
        startDateInputView=view.findViewById(R.id.dateTextView);
        startTimeInputView=view.findViewById(R.id.timeInputTextView);
        endDateInputView=view.findViewById(R.id.endDateInputTextView);
        endTimeInputView=view.findViewById(R.id.endTimeInputTextVIew);
        endDateInputView.setVisibility(View.INVISIBLE);
        endTimeInputView.setVisibility(View.INVISIBLE);

        createNotificationManager();

        Bundle bundle=this.getArguments();
        maxID=bundle.getInt(notificationTagKey);
        venueName=bundle.getStringArrayList(venueNameKey);


        String[] venueNameArray=new String[venueName.size()];
        for (int i = 0 ;i<venueName.size();i++){
            venueNameArray[i]=venueName.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item,venueNameArray);
        venueEditText.setAdapter(adapter);
        venueEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                venueEditText.showDropDown();
                return false;
            }
        });

        scheduleViewModel=new ViewModelProvider(this).get(ScheduleViewModel.class);
        venueViewModel= new ViewModelProvider(this).get(VenueViewModel.class);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event = eventEditText.getText().toString();
                venue = venueEditText.getText().toString();
                boolean bool= startDate.getTime() > endDate.getTime();
                Log.e("Time compare",Long.toString(startDate.getTime())+" "+Long.toString(endDate.getTime()));
                Log.e("Time compare",Boolean.toString(bool));
                if (startDate.getTime() > endDate.getTime()) {
                    Toast.makeText(getActivity(), "Start date cannot earlier than end date", Toast.LENGTH_LONG).show();
                }
                else {

                    Log.e("Schedule id", Integer.toString(maxID + 1));
                    Schedule schedule = new Schedule(maxID + 1, event, venue, startDate, endDate, isRepeat);
                    scheduleViewModel.insert(schedule);

                    Venue venueObject = new Venue(0, venue);
                    venueViewModel.insert(venueObject);
                    getFragmentManager().popBackStack();

                    Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
                    Log.e("Event", event);
                    intent.putExtra(notificationKey, event);
                    intent.putExtra(notificationContentKey, venue);
                    intent.putExtra(notificationTagKey, Integer.toString(maxID + 1));
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), maxID + 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

                    if (isRepeat) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, endDate.getTime(), pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, startDate.getTime(), pendingIntent);
                    }
                }
            }
        });
        startDateInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog(0);
            }
        });
        startTimeInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog(0);

            }
        });
        endDateInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog(1);
            }
        });
        endTimeInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog(1);
            }
        });
        isRepeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    endDateInputView.setVisibility(View.VISIBLE);
                    endTimeInputView.setVisibility(View.VISIBLE);
                    endDateView.setVisibility(View.VISIBLE);
                    endTimeView.setVisibility(View.VISIBLE);
                    isRepeat=true;
                }
                else{
                    endDateInputView.setVisibility(View.INVISIBLE);
                    endTimeInputView.setVisibility(View.INVISIBLE);
                    endDateView.setVisibility(View.INVISIBLE);
                    endTimeView.setVisibility(View.INVISIBLE);
                    isRepeat=false;
                }
            }
        });
        listener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                startDate=cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDateView.setText(dateFormat.format(startDate.getTime()));

            }
        };
        listener1= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                endDate=cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                endDateView.setText(dateFormat.format(endDate.getTime()));

            }
        };
        timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(hourOfDay,minute);
                startDate.setHours(hourOfDay);
                startDate.setMinutes(minute);
                startDate.setSeconds(0);
                DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                startTimeView.setText(dateFormat.format(startDate.getTime()));
            }
        };
        timeListener1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(hourOfDay,minute);
                endDate.setHours(hourOfDay);
                endDate.setMinutes(minute);
                endDate.setSeconds(0);
                DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                endTimeView.setText(dateFormat.format(endDate.getTime()));
            }
        };
        return view;

    }
    private void datePickerDialog(int date){
        if (date==0) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), listener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
        else{
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), listener1,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }
    private void timePickerDialog(int time){
        if (time==0) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeListener,
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }
        else{
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeListener1,
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }
    }
    private void createNotificationManager(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence channelName = "ScheduleChannel";
            String description = "Schedule reminder for the study support application";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notificationSchedule",channelName,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
