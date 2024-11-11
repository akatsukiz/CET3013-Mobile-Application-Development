package com.example.week2cet3013

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var text_username:EditText
    private lateinit var text_password:EditText
    private lateinit var button_login:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set the reference to the GUI elements
        text_username = findViewById(R.id.text_username)
        text_password = findViewById(R.id.text_password)
        button_login = findViewById(R.id.button_login)

        //provide a simple listener to the button
        button_login.setOnClickListener{
            //provide the action at here
            //Toast.makeText(this,"Under Construction!",Toast.LENGTH_LONG).show()

            //get the username and password
            val name = text_username.text
            val password = text_password.text

            if(name.trim().isEmpty() || password.trim().isEmpty()) //trim is remove empty spaces
            {
                Toast.makeText(this,"Name or password is empty!",Toast.LENGTH_LONG).show()
            }
            else
            {
                //check the number of letter for the username
                if(name.length<4)
                {
                    //display the error to the user
                    text_username.error="Poor User Name!"
                }
                else if(password.length<4)
                {
                    text_password.error="Weak Password!"
                }
                else
                {
                    Toast.makeText(this,"Welcome $name",Toast.LENGTH_LONG).show()
                    Log.d("Week2", "Login Success!")
                }
            }
        }
    }
}