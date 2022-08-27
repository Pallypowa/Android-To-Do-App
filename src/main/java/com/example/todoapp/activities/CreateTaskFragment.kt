package com.example.todoapp.activities

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.todoapp.R
import kotlinx.android.synthetic.main.activity_task_create.*
import kotlinx.android.synthetic.main.activity_task_create.view.*
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class CreateTaskFragment(private val listener: CreateTaskListener): DialogFragment() {

    interface CreateTaskListener{
        fun onCreateTask(task: String)
    }

    @RequiresApi(Build.VERSION_CODES.N)
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

        rootView.cancel_button.setOnClickListener {
            dismiss()
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