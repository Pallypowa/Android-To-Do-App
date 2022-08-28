package com.example.todoapp.data

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.activities.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferencesHandler(private val activity: Activity) {

    fun saveData(sharedPrefTask: String,
                        sharedPrefLtext: String,
                        data: ArrayList<Task>){
        val sharedPreferences = activity.getSharedPreferences("shared preferences",
            AppCompatActivity.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        val gson = Gson()
        var json: String = gson.toJson(data)

        editor.putString(sharedPrefTask, json)

        json = gson.toJson(MainActivity.longTexts)

        editor.putString(sharedPrefLtext, json)

        editor.apply()

    }

    fun loadData(sharedPrefTask: String,
                 sharedPrefLtext: String): ArrayList<Task>
    {
        val sharedPreferences = activity.getSharedPreferences("shared preferences",
            AppCompatActivity.MODE_PRIVATE
        )

        val gson = Gson()

        var json = sharedPreferences.getString(sharedPrefTask, null)

        var type: Type = object : TypeToken<ArrayList<Task>>() {}.type

        val data: ArrayList<Task> = gson.fromJson(json, type)

        /* Have to initialize in the MainActivity if it's empty...
        if (data == null) {
            data = ArrayList()
        }

         */

        json = sharedPreferences.getString(sharedPrefLtext, null)

        type = object : TypeToken<HashMap<Int, String>>() {}.type

        val temp: HashMap<Int, String>? = gson.fromJson(json, type)
        if (temp != null) MainActivity.longTexts = temp

        return data

    }
}