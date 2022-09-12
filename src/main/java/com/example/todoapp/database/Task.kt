package com.example.todoapp.database

data class Task(var taskText: String, var done: Boolean, var deadline: String, var categoryId: Int, var longText: String)
