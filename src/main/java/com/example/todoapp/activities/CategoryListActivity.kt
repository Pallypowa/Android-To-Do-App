package com.example.todoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapters.CategoryListAdapter
import com.example.todoapp.data.Category
import kotlinx.android.synthetic.main.activity_category_list.*
import kotlin.collections.ArrayList

class CategoryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        val categoryList: ArrayList<Category> = ArrayList()
        categoryList.add(Category(0, "Work", 6, "#000000"))
        categoryList.add(Category(1, "Learning", 6, "#000000"))
        categoryList.add(Category(2, "Groceries", 3, "#000000"))
        categoryList.add(Category(3, "Work2", 2, "#000000"))

        val recyclerView = category_recview

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CategoryListAdapter(categoryList)

        recyclerView.adapter = adapter
    }
}