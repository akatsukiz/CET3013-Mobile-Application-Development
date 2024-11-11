package com.example.week7cet3013fragmentnavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController

class MessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_message, container, false)

        val nextButton: Button = view.findViewById<Button>(R.id.button_next)
        val textMessage: EditText = view.findViewById<EditText>(R.id.text_message)

        nextButton.setOnClickListener {
            /*
            view.findNavController()
                .navigate(R.id.action_messageFragment_to_encryptFragment) */
                val message = textMessage.text.toString()
                val action = MessageFragmentDirections
                    .actionMessageFragmentToEncryptFragment(message)
                view.findNavController().navigate(action)
        }
        return view
    }


}