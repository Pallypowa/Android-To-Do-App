package com.example.todoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R

class CreateTaskActivity: AppCompatActivity() {
    private var listener: CreateTaskFragment.CreateTaskListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_create)
    }
}