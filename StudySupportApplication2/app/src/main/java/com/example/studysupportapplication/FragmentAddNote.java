package com.example.studysupportapplication;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FragmentAddNote extends Fragment {
    Button submitBtn, imgBtn;
    Switch isImageSwitch;
    int imgCode=200;
    EditText courseEditText, subjectEditText, chapterEditText, noteTitleEditText, noteContentEditText;
    String courseName="";
    String subjectTitle="";
    int chapterNum=0;
    String noteTitle="";
    String noteContent="";
    boolean isImage=false;
    String noteImage="";
    Bitmap bitmap=null;
    NoteViewModel noteViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_addnote,container, false);
        submitBtn=view.findViewById(R.id.submitButton);
        courseEditText=view.findViewById(R.id.eventEditText);
        subjectEditText=view.findViewById(R.id.venueEditText);
        chapterEditText=view.findViewById(R.id.chapterEditText);
        noteTitleEditText=view.findViewById(R.id.noteTitleEditText);
        noteContentEditText=view.findViewById(R.id.noteContentEditText);
        imgBtn=view.findViewById(R.id.imageButton);
        isImageSwitch=view.findViewById(R.id.isImageSwitch);
        imgBtn.setVisibility(View.INVISIBLE);

        isImageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isImage=isChecked;
                if (isChecked){
                    imgBtn.setVisibility(View.VISIBLE);
                    noteContentEditText.setVisibility(View.INVISIBLE);
                }
                else{
                    imgBtn.setVisibility(View.INVISIBLE);
                    noteContentEditText.setVisibility(View.VISIBLE);
                }
            }
        });
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser( intent,"Select Image"), imgCode);
            }
        });
        noteViewModel=new ViewModelProvider(this).get(NoteViewModel.class);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseName=courseEditText.getText().toString();
                subjectTitle=subjectEditText.getText().toString();
                chapterNum=Integer.parseInt(String.valueOf(chapterEditText.getText()));
                noteTitle=noteTitleEditText.getText().toString();
                noteContent=noteContentEditText.getText().toString();

                Note note = new Note(courseName,subjectTitle,chapterNum,noteTitle,isImage,noteContent,String.valueOf(noteImage));
                noteViewModel.insert(note);
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode,resultCode,data);
    Log.e("Activity result","got result");
    if(resultCode==RESULT_OK){
        Uri selectedImageUri = data.getData();


            try{
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);
                noteImage= convertBitmap(bitmap);
                Log.e("Show image value", noteImage);
            }catch(IOException e){
                e.printStackTrace();
                Log.e("Did not get Image Error","No Image");
            }

    }
    }
    public String convertBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b =byteArrayOutputStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
