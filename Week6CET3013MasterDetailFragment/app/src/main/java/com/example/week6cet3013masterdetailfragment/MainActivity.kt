package com.example.week6cet3013masterdetailfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.week6cet3013masterdetailfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MasterFragment.OnMasterSelectedListener {
    var mDualPane = false
    private var frameLayout: FrameLayout? = null
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var masterFragment: MasterFragment? = null //Country ListView
        frameLayout = binding.frameLayout //The frameLayout in the MainActivity

        //If now is in the portrait mode
        if (frameLayout != null) {
            mDualPane = false
            val fragmentTransaction = supportFragmentManager.beginTransaction()

            //double check the master fragment already loaded in the frameLayout
            masterFragment = supportFragmentManager.findFragmentByTag("MASTER") as MasterFragment?

            if (masterFragment == null) {
                masterFragment = MasterFragment()
                //add the master fragment in the framelayout
                fragmentTransaction.add(R.id.frameLayout, masterFragment, "MASTER")
            }
            fragmentTransaction.commit()
        } else {
            //This is under landscape mode
            mDualPane = true

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            //Test is it first time loading
            masterFragment = supportFragmentManager
                .findFragmentById(R.id.frameLayoutMaster) as MasterFragment?

            if (masterFragment == null) { //If first time loading, load the masterFragment
                masterFragment = MasterFragment()
                fragmentTransaction.add(R.id.frameLayoutMaster, masterFragment)
            }
            var detailFragment = supportFragmentManager
                .findFragmentById(R.id.frameLayoutDetail) as DetailFragment?

            if (detailFragment == null) { //If first time loading, load the DetailFragment
                detailFragment = DetailFragment()
                fragmentTransaction.add(R.id.frameLayoutDetail, detailFragment)
            }
            fragmentTransaction.commit()
        }

        masterFragment!!.setOnMasterSelectedListener(
            object: MasterFragment.OnMasterSelectedListener {
                override fun onItemSelected(countryName: String?) {
                    if (countryName != null) {
                        sendCountryName(countryName)
                    }
                }
            }
        )
    } //end of onCreate

        private fun sendCountryName(countryName: String) {
            val detailFragment: DetailFragment?
            if (mDualPane) {
                //Two pane layout
                detailFragment =
                    supportFragmentManager.findFragmentById(R.id.frameLayoutDetail) as DetailFragment?
                detailFragment!!.showSelectedCountry(countryName)
            } else {
                // Single pane layout
                detailFragment = DetailFragment()
                val bundle = Bundle()
                bundle.putString(DetailFragment.KEY_COUNTRY_NAME, countryName)
                detailFragment.arguments = bundle
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameLayout, detailFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

    override fun onItemSelected(countryName: String?) {
        TODO("Not yet implemented")
    }
}
