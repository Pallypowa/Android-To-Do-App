package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_long_text.*

class LongTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_text)

        task_name.text = intent.getStringExtra("task_name")
        val index = intent.getIntExtra("index", -1)
        val longText = intent.getStringExtra("long_text")
        if(!longText.isNullOrEmpty()) long_text_view.text.insert(0, longText)

        back_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            if(index != -1){
                intent.putExtra("index", index)
                intent.putExtra("long_text", long_text_view.text.toString())
            }
            startActivity(intent)
        }

        reminder_button.setOnClickListener{
            Snackbar.make(card_view, "Not yet implemented!", Snackbar.LENGTH_SHORT).show()
        }
    }
}