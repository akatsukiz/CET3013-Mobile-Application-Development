package com.example.week12cet3013animationpropertydemo

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import com.example.week12cet3013animationpropertydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var sunset = false
    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scene.setOnClickListener {
            if (sunset == false)
                startAnimation()
            else
                reverseAnimation()
        }
    }
    private fun startAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        Log.d("Test", "sunYStart:" + sunYStart )
        Log.d("Test", "sunYEnd:" + sunYEnd )

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)

        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(3000)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)

        animatorSet.start()
        sunset = true
    }
    private fun reverseAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        Log.d("Test", "sunYStart:" + sunYStart )
        Log.d("Test", "sunYEnd:" + sunYEnd )

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYEnd, sunYStart)
            .setDuration(3000)

        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(3000)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(nightSkyAnimator)
            .before(sunsetSkyAnimator)

        animatorSet.start()
        sunset = false
    }
}