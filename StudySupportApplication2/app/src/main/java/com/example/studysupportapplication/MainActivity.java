package com.example.studysupportapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studysupportapplication.ui.main.SectionsPagerAdapter;
import com.example.studysupportapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NoteDataListener{

    private ActivityMainBinding binding;
    private String courseName;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentHome());

        binding.bottomNavigationView.setOnItemSelectedListener(item->{
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new FragmentHome());
                    break;

            }
            return true;
        });



        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSendData(String title, String content, boolean bool) {
        if(!bool) {
            FragmentNoteContent fragmentNoteContent = new FragmentNoteContent();
            Bundle args = new Bundle();
            args.putString(FragmentNoteContent.contentKey, content);
            args.putString(FragmentNoteContent.titleKey, title);


            fragmentNoteContent.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragmentNoteContent).commit();
        }
        else{
            FragmentNoteContentImage fragmentNoteContentImage = new FragmentNoteContentImage();
            Bundle args = new Bundle();
            args.putString(FragmentNoteContentImage.contentKey, content);
            args.putString(FragmentNoteContentImage.titleKey, title);


            fragmentNoteContentImage.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragmentNoteContentImage).commit();
        }
    }

}