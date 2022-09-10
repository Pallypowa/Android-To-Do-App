package com.example.todoapp.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapters.CategoryListAdapter
import com.example.todoapp.data.Category
import com.example.todoapp.data.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_category_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_text_layout.view.*
import kotlin.collections.ArrayList

class CategoryListActivity : AppCompatActivity(), CategoryListAdapter.OnCategoryClickListener {
    private val categoryList: ArrayList<Category> = ArrayList()
    private var adapter: CategoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        categoryList.add(Category(0, "Work", 6, "#000000"))
        categoryList.add(Category(1, "Learning", 6, "#000000"))
        categoryList.add(Category(2, "Groceries", 3, "#000000"))
        categoryList.add(Category(3, "Work2", 2, "#000000"))
        addNewItemCard(categoryList)

        val recyclerView = category_recview

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CategoryListAdapter(categoryList, this)

        recyclerView.adapter = adapter
    }

    //Always call after filling in the data
    private fun addNewItemCard(categoryList: ArrayList<Category>){
        categoryList.add(Category(categoryList.size, "Add a new list", -1, "#000000"))
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Title")
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.edit_text
        builder.setView(dialogLayout)
        val alertDialog = builder.create()
        builder.setPositiveButton("Add") { _, _ ->
            //addCategory()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            alertDialog.cancel()
        }

        builder.show()
    }

    private fun addCategory(){
        categoryList.removeLast()
        categoryList.add(Category(5, "category", 3, "#000000"))
        addNewItemCard(categoryList)
        adapter?.notifyDataSetChanged()
    }

    override fun onCardClicked(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onAddCardClicked(position: Int) {
        showAlertDialog()
    }
}