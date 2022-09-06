package com.example.todoapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Category

class CategoryListAdapter(private var categoryList: List<Category>): RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.category_name_text)
        val itemNum: TextView = itemView.findViewById(R.id.category_itemnum_text)
        val categoryColor: ImageView = itemView.findViewById(R.id.category_color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.card_view_category, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]

        holder.categoryName.text = category.categoryName
        holder.itemNum.text = category.taskCount.toString()
        holder.categoryColor.setBackgroundColor(Color.parseColor(category.color))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}