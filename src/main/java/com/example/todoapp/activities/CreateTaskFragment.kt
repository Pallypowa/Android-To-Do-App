package com.example.todoapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_task_create.*
import kotlinx.android.synthetic.main.activity_task_create.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class CreateTaskFragment(private val listener: CreateTaskListener): DialogFragment() {

    interface CreateTaskListener{
        fun onCreateTask(task: String, longText: String, deadline: String)
    }

    private var date: String = ""
    private var time: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.activity_task_create, container, false)

        rootView.create_button.setOnClickListener{
            listener.onCreateTask(task_title.text.toString(), task_longtext.text.toString(), "$date $time")
            dismiss()
        }

        rootView.cancel_button.setOnClickListener {
            dismiss()
        }

        rootView.date_switch.setOnClickListener {
            val datePickerDialog = DatePickerDialog(rootView.context)
            datePickerDialog.setOnDateSetListener { _, i, i2, i3 ->
                val selectedTime = LocalDateTime.of(i, i2 + 1, i3, LocalDateTime.now().hour, LocalDateTime.now().minute)
                if(isDateInPast(selectedTime)){
                    Snackbar.make(rootView, "You can't set a deadline for the past!", Snackbar.LENGTH_SHORT).show()
                    date_text.text = ""
                    hideText(date_text)
                }
                else{
                    date_text.text = "$i/${padWithZeros(i2 + 1)}/${padWithZeros(i3)}"
                    showText(date_text)
                    date = date_text.text.toString()
                }

            }
            datePickerDialog.setOnDismissListener {
                rootView.date_switch.isChecked = false
            }
            if(rootView.date_switch.isChecked){
                datePickerDialog.show()
            }
            else {
                hideText(date_text)
            }
        }

        rootView.time_switch.setOnClickListener {
            var hours: Int = 0
            var mins: Int = 0
            val listener = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                hours = i
                mins = i2
                time_text.text = "${padWithZeros(hours)}:${padWithZeros(mins)}"
                showText(time_text)
                time = time_text.text.toString()
            }
            val timePickerDialog = TimePickerDialog(rootView.context,
                listener,
                12,
                0,
                true)
            timePickerDialog.setOnDismissListener{
                rootView.time_switch.isChecked = false
            }
            if (rootView.time_switch.isChecked){
                timePickerDialog.show()
            }
            else{
                hideText(time_text)
            }

        }

        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isDateInPast(date: LocalDateTime): Boolean{
        val currentDate = LocalDateTime.now()
        return date.isBefore(currentDate)
    }

    private fun hideText(textView: TextView){
        textView.visibility = View.INVISIBLE
    }

    private fun showText(textView: TextView){
        textView.visibility = View.VISIBLE
    }

    private fun padWithZeros(time: Int): String{
        val timeString = time.toString()
        if(timeString.length == 1) return "0$timeString"
        return timeString
    }
}