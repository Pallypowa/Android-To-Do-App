package com.example.todoapp.adapters

import android.graphics.Paint
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Task
import kotlinx.android.synthetic.main.card_view_task.view.*

class TaskListAdapter(private var dataList: List<Task>, private var listener: OnTaskClickListener):
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>(){

    interface OnTaskClickListener{
        fun onCheckBoxClicked(position: Int, itemView: View)
        fun onCardClicked(position: Int, itemView: View)
        fun onEditTextChanged(position: Int, text: String)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val taskText: EditText = itemView.findViewById(R.id.task_text)
        val isDone: CheckBox = itemView.findViewById(R.id.checkBox)
        val deadline: TextView = itemView.findViewById(R.id.deadline_text)

        fun bind(holder: ViewHolder, position: Int, listener: OnTaskClickListener, task: Task){
            holder.taskText.setText(task.taskText)
            holder.isDone.isChecked = task.done
            holder.deadline.text = task.deadline

            if(holder.isDone.isChecked)
                holder.taskText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            if(itemView.checkBox.isChecked) itemView.alpha = 0.3f else itemView.alpha = 1.0f
            isDone.setOnClickListener {
                listener.onCheckBoxClicked(position, itemView)
            }
            itemView.setOnClickListener{
                listener.onCardClicked(position, itemView)
            }

            //If focus has changed...
            taskText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    listener.onEditTextChanged(position, taskText.text.toString())

                }
            }

            //If enter is pressed...
            taskText.setOnEditorActionListener{view, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    keyEvent == null ||
                    keyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                    listener.onEditTextChanged(position, taskText.text.toString())
                    true
                }
                false
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = dataList[position]

        holder.bind(holder, position, listener, task)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}