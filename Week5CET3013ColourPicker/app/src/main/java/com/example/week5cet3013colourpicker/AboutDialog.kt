package com.example.week5cet3013colourpicker

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AboutDialog:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.about_message))
            .setTitle(getString(R.string.about_title))
            .setPositiveButton("OK") {_,_->}
            .setNegativeButton("Cancel") {_,_->}
            .create()
        return dialog
    }

}