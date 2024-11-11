package com.example.tracemykid

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.tracemykid.data.KidActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class SettingFragment : Fragment() {
    private lateinit var mKidActivityViewModel:KidActivityViewModel
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        val context = requireActivity().applicationContext
        val sharedpreference= requireActivity().getSharedPreferences("skipAnimation",MODE_PRIVATE)
        var skipAnimation = sharedpreference.getBoolean("skipAnimation", false)

        mKidActivityViewModel = ViewModelProvider(this).get(KidActivityViewModel::class.java)
        // For navigation bar
        val navigationBar = view.findViewById<BottomNavigationView>(R.id.navigation_below3)
        navigationBar.selectedItemId = R.id.menu_setting
        navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_add -> {
                    view.findNavController().navigate(R.id.action_settingFragment_to_addActivityFragment)
                    true
                }

                R.id.menu_search -> {
                    view.findNavController().navigate(R.id.action_settingFragment_to_searchActivityFragment)
                    true
                }

                R.id.menu_setting -> {
                    true
                }

                else -> {
                    false
                }
            }
        }

        val switchEnable = view.findViewById<Switch>(R.id.switch_enableWelcome)
        switchEnable.isChecked = !skipAnimation
        switchEnable.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.e("OncheckedChange skip", "Checked")
            skipAnimation = !skipAnimation
            val editor = sharedpreference.edit()
            editor.apply{
                putBoolean("skipAnimation",skipAnimation)
            }
            editor.commit()
        }


        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage(this.toString())
            .setPositiveButton("Confirm", DialogInterface.OnClickListener {
                    dialog, id ->
                mKidActivityViewModel.deleteAll()
                Toast.makeText(context,
                    R.string.delete, Toast.LENGTH_SHORT).show()
                dialog.dismiss() })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss() })

        val alert = dialogBuilder.create()
        alert.setTitle("Delete Data Confirmation")
        alert.setMessage("Are you sure you want to delete all data?")
        val clearAllButton = view.findViewById<Button>(R.id.button_clear_all)
        clearAllButton.setOnClickListener {
            alert.show()
        }

        return view
    }

}