package com.example.todoapp

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_view_task.view.*

class RecyclerViewAdapter(private var dataList: List<Task>, private var listener: OnItemClickListener):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun onCheckBoxClicked(position: Int, itemView: View)
        fun onCardClicked(position: Int, itemView: View)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val taskText: TextView = itemView.findViewById(R.id.task_text)
        val isDone: CheckBox = itemView.findViewById(R.id.checkBox)

        fun bind(holder: ViewHolder,position: Int, listener: OnItemClickListener, task: Task){
            holder.taskText.text = task.taskText
            holder.isDone.isChecked = task.done

            if(holder.isDone.isChecked)
                holder.taskText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            if(itemView.checkBox.isChecked) itemView.alpha = 0.3f else itemView.alpha = 1.0f
            isDone.setOnClickListener {
                listener.onCheckBoxClicked(position, itemView)
            }
            itemView.setOnClickListener{
                listener.onCardClicked(position, itemView)
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