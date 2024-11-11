package com.example.tracemykid
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.tracemykid.databinding.ActivityWelcomeScreenBinding


class WelcomeScreen : AppCompatActivity(){
    private lateinit var binding: ActivityWelcomeScreenBinding

    private val splashScreenTime = 6000L // Delay Welcome Screen for 6 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharePref = getSharedPreferences("skipAnimation", MODE_PRIVATE)
        val editor = sharePref.edit()
        var skipAnimation = sharePref.getBoolean("skipAnimation",false)
        Log.e("skipAnimation0", skipAnimation.toString())
        binding.checkboxDisable.setOnCheckedChangeListener { buttonView, isChecked ->
            skipAnimation = !skipAnimation
            Log.e("skipAnimation",skipAnimation.toString())
            editor.apply{
                putBoolean("skipAnimation", skipAnimation)
            }
            editor.commit()
        }




        skipAnimation = sharePref.getBoolean("skipAnimation", false)
        if (skipAnimation)
        {
            // Navigate to main screen
            navigateDirectToMainScreen()
        }
        else {
            splashScreen.setSplashScreenTheme(R.style.SplashScreenTheme)

            binding.textAppSlogan.isVisible = false

            // App Logo Animation
            val animScaleUp = AnimationUtils.loadAnimation(applicationContext, R.anim.scale_up)
            animScaleUp.duration=2000
            binding.drawableLogo.startAnimation(animScaleUp)

            // App Name Animation
            val animTranslate = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_up)
            animTranslate.duration = 2000
            binding.textAppName.startAnimation(animTranslate)

            // Set animation listener on animBounce
            animTranslate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // This method is called when the animation starts
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // This method is called when the animation ends
                    // Start the translate animation after animBounce ends
                    binding.textAppSlogan.isVisible = true
                    startTranslateAnimation()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // This method is called when the animation repeats
                }
            })

            // Navigate to main screen
            navigateToMainScreen()
        }
    }

    // Navigate user to main activity
    private fun navigateDirectToMainScreen(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToMainScreen() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashScreenTime)
    }
    // App Slogan Animation
    fun startTranslateAnimation() {
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        animFadeIn.duration = 3500
        binding.textAppSlogan.startAnimation(animFadeIn)
    }
}

