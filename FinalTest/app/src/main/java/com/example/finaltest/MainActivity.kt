package com.example.finaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.View

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Assume you have a view with the ID "myView" in your layout
        val myView: View = findViewById(R.id.myView)

        // Create ObjectAnimators for translation and rotation
        val translationAnimator = ObjectAnimator.ofFloat(myView, "translationY", 0f, 200f)
        val rotationAnimator = ObjectAnimator.ofFloat(myView, "rotation", 0f, 360f)

        // Set the duration for each animator
        translationAnimator.duration = 1000
        rotationAnimator.duration = 1000

        // Create an AnimatorSet and play the animations together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationAnimator, rotationAnimator)

        // Start the animation
        animatorSet.start()
    }
}
