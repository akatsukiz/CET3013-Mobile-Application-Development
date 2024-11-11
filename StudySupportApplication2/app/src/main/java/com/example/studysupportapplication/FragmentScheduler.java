package com.example.studysupportapplication;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentScheduler extends Fragment {
    private ScheduleViewModel scheduleViewModel;
    private VenueViewModel venueViewModel;
    private RecyclerView recylerView;
    private int location=0;
    private String courseName="", subjectTitle="", noteTitle="";
    private int chapter=0;
    private int maxID;

    private ScheduleListAdapter adapter=null;
    private TextView textView;
    List<Schedule> schedule01;
    ArrayList<String> venueName = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule,container,false);
        FloatingActionButton fab;
        fab= (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        scheduleViewModel=new ViewModelProvider(this).get(ScheduleViewModel.class);
        venueViewModel=new ViewModelProvider(this).get(VenueViewModel.class);
        adapter= new ScheduleListAdapter(getContext());
        recylerView=view.findViewById(R.id.scheduleRecyclerView);
        recylerView.setAdapter(adapter);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textView= view.findViewById(R.id.titleTextView);

        scheduleViewModel.getId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.e("Integer",String.valueOf(integer));
                if (integer!=null){
                    maxID=integer.intValue();
                }
                else{
                    maxID=0;
                }
            }
        });

        scheduleViewModel.getAllDetail().observe(getViewLifecycleOwner(), new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedule) {
                Log.e("Schedule -1", Integer.toString(schedule.size()));

                for (int i = 0; i < schedule.size(); i++) {
                    if (schedule.get(i).getIsRepeat()) {
                        Date date = schedule.get(i).getEndDateTime();
                        if (date.getTime()<System.currentTimeMillis()) {
                            scheduleViewModel.delete(schedule.get(i));
                        }
                        Log.e("Test", "Got into switch 0");
                    }
                    else{
                        Date date = schedule.get(i).getStartDateTime();
                        if (date.getTime()<System.currentTimeMillis()){
                            scheduleViewModel.delete(schedule.get(i));
                        }
                    }
                }
            }
        });

        scheduleViewModel.getAllDetail().observe(getViewLifecycleOwner(), new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedule) {
                Log.e("Schedule 0", Integer.toString(schedule.size()));
                adapter.setSchedule(schedule);
                Log.e("Test","Got into switch 0");
            }
        });

        venueViewModel.getAllVenue().observe(getViewLifecycleOwner(), new Observer<List<Venue>>() {
            @Override
            public void onChanged(List<Venue> venues) {
                Log.e("Venue count:", Integer.toString(venues.size()));
                if (venues != null) {
                    for (int i=0; i<venues.size(); i++) {
                        venueName.add(venues.get(i).getVenueName());
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt(FragmentAddSchedule.notificationTagKey,maxID);
                bundle.putStringArrayList(FragmentAddSchedule.venueNameKey,venueName);
                FragmentAddSchedule fragmentAddSchedule = new FragmentAddSchedule();
                fragmentAddSchedule.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentAddSchedule).addToBackStack(null).commit();

            }
        });

        return view;

    }



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}

