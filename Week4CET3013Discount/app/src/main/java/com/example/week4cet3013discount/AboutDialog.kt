package com.example.week4cet3013discount

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AboutDialog:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.about_message))
            .setPositiveButton("OK") {_,_ ->}
            .setNegativeButton("Cancel") {_, _ ->}
            .create()
        return dialog
    }
}