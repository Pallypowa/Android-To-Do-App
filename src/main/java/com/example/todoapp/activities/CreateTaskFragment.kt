package com.example.todoapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_task_create.*
import kotlinx.android.synthetic.main.activity_task_create.view.*
class CreateTaskFragment(private val listener: CreateTaskListener): DialogFragment() {

    interface CreateTaskListener{
        fun onCreateTask(task: String, longText: String)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.activity_task_create, container, false)

        rootView.create_button.setOnClickListener{
            listener.onCreateTask(task_title.text.toString(), task_longtext.text.toString())
            dismiss()
        }

        rootView.cancel_button.setOnClickListener {
            dismiss()
        }

        rootView.date_switch.setOnClickListener {
            val datePickerDialog = DatePickerDialog(rootView.context)
            if(rootView.date_switch.isChecked){
                datePickerDialog.show()
                datePickerDialog.setOnDateSetListener { datePicker, i, i2, i3 ->
                    Snackbar.make(rootView, "Date: $i/$i2/$i3", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        rootView.time_switch.setOnClickListener {
            val timePickerDialog = TimePickerDialog(rootView.context,
                { timePicker, i, i2 ->  },
                12,
                0,
                true)
            if (rootView.time_switch.isChecked){
                timePickerDialog.show()
            }

        }


//        rootView.set_deadline.setOnClickListener {
//            val c = Calendar.getInstance()
//            val year = c.get(Calendar.YEAR)
//            val month = c.get(Calendar.MONTH)
//            val day = c.get(Calendar.DAY_OF_MONTH)
//            val datePickerDialog = DatePickerDialog(rootView.context, R.style.MySpinnerDatePickerStyle,
//                { _, year, monthOfYear, dayOfMonth ->
//
//                    // Display Selected date in textbox
//                    text_deadline.setText("$dayOfMonth $month, $year")
//
//                }, year, month, day)
//            datePickerDialog.show()
//
//            datePickerDialog.setOnDateSetListener { _, i, i2, i3 ->
//                text_deadline.text = "deadline: $i/$i2/$i3"
//            }
//        }

        return rootView
    }
}