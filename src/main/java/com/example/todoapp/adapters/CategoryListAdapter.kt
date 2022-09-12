package com.example.todoapp.adapters

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.Category
import com.google.android.material.snackbar.Snackbar

class CategoryListAdapter(private val categoryList: List<Category>,
                          private val listener: OnCategoryClickListener): RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    interface OnCategoryClickListener{
        fun onCardClicked(position: Int)
        fun onAddCardClicked(position: Int)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.category_name_text)
        val itemNum: TextView = itemView.findViewById(R.id.category_itemnum_text)
        val categoryColor: ImageView = itemView.findViewById(R.id.category_color)

        fun bindData(category: Category,
                     position: Int,
                     categoryListSize: Int,
                     listener: OnCategoryClickListener){
            setListeners(position, categoryListSize, listener)

            this.categoryName.text = category.categoryName
            this.categoryColor.setBackgroundColor(Color.parseColor(category.color))

            if(category.taskCount != -1){
                this.itemNum.text = category.taskCount.toString()
                return
            }

            //If the item is the "Add new category...
            itemView.alpha = 0.65f
            this.itemNum.visibility = View.GONE
            this.categoryName.gravity = Gravity.CENTER_VERTICAL
            this.categoryColor.setImageResource(R.drawable.ic_baseline_add_24)
            this.categoryColor.setBackgroundColor(Color.WHITE)

        }

        private fun setListeners(position: Int,
                                 categoryListSize: Int,
                                 listener: OnCategoryClickListener){
            itemView.setOnClickListener {
                //TODO: Implement opening tasks/adding new list
                if (position == categoryListSize - 1){
                    Snackbar.make(itemView, "New list...", Snackbar.LENGTH_SHORT).show()
                    listener.onAddCardClicked(position)
                }
                else {
                    Snackbar.make(itemView, "Bringing to tasks...", Snackbar.LENGTH_SHORT).show()
                    //listener.onCardClicked(position)
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.card_view_category, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]

        holder.bindData(category, position, categoryList.size, listener)

//        holder.categoryName.text = category.categoryName
//        holder.itemNum.text = category.taskCount.toString()
//        holder.categoryColor.setBackgroundColor(Color.parseColor(category.color))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}