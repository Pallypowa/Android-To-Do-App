package com.example.todoapp.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import kotlinx.android.synthetic.main.activity_task_create.*
import kotlinx.android.synthetic.main.activity_task_create.view.*

class CreateTaskFragment(private val listener: CreateTaskListener): DialogFragment() {

    interface CreateTaskListener{
        fun onCreateTask(task: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.activity_task_create, container, false)

        rootView.create_button.setOnClickListener{
            listener.onCreateTask(task_text2.text.toString())
            dismiss()
        }
        return rootView
    }
}