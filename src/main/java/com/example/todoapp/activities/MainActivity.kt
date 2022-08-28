package com.example.todoapp.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.RecyclerViewAdapter
import com.example.todoapp.data.SharedPreferencesHandler
import com.example.todoapp.data.Task
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_view_task.view.*
import kotlinx.android.synthetic.main.edit_text_layout.view.*
import java.lang.reflect.Type

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener,
    CreateTaskFragment.CreateTaskListener {

    //To achieve static variable functionality...
    companion object {
        var longTexts: HashMap<Int, String> = HashMap()
    }

    private val ALERT_TITLE = "Please enter your task!"
    private val RENAME_TITLE = "Edit"
    private val SHARED_PREF_TASKS = "TASKS"
    private val SHARED_PREF_LTEXT = "LTEXT"
    private var adapter: RecyclerViewAdapter? = null
    private var data: ArrayList<Task>? = null
    private var recyclerView: RecyclerView? = null
    private var dragged: Boolean = false
    private var sharedPrefHandler: SharedPreferencesHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefHandler = SharedPreferencesHandler(this)

        //Read data from SharedPreferences
        //loadData()

        data = sharedPrefHandler?.loadData(SHARED_PREF_TASKS, SHARED_PREF_LTEXT)

        if(data == null){
            data = ArrayList()
        }

        //Initialize recycler view...
        recyclerView = recViewId
        recyclerView?.layoutManager = LinearLayoutManager(this)

        adapter = RecyclerViewAdapter(data!!, this)
        recyclerView?.adapter = adapter

        val taskIndex = intent.getIntExtra("index", -1)
        val taskLongText: String? = intent.getStringExtra("long_text")

        if (taskIndex != -1 && !taskLongText.isNullOrEmpty()) {
            longTexts[taskIndex] = taskLongText
        }

        //generateTestData(0, 10)

        setTaskNumber()

        //Gesture listener
        val myCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val recAdapter = recyclerView.adapter as RecyclerViewAdapter

                val from = viewHolder.adapterPosition

                val to = target.adapterPosition

                recAdapter.notifyItemMoved(from, to)

                val originalPosData: Task = data!![from]
                val otherData: Task = data!![to]

                data!![to] = originalPosData
                data!![from] = otherData

                dragged = true
                return true
            }

            override fun onMoved(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                fromPos: Int,
                target: RecyclerView.ViewHolder,
                toPos: Int,
                x: Int,
                y: Int
            ) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                Log.d("longtexts:", "positions: $fromPos $toPos $longTexts")
                updateLongTextMap(fromPos, toPos)
                if (!dragged || fromPos == toPos) viewHolder.itemView.alpha = 1.0f
                //recyclerView.scrollToPosition(toPos)
            }


            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (dragged) {
                    resetAdapter()
                    dragged = false
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val background = ColorDrawable(Color.RED)
                background.setBounds(
                    0,
                    viewHolder.itemView.top,
                    viewHolder.itemView.left + dX.toInt(),
                    viewHolder.itemView.bottom
                )
                background.draw(c)

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val holder = viewHolder as RecyclerViewAdapter.ViewHolder
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        data?.removeAt(holder.adapterPosition)
                        removeLongTexts(holder.adapterPosition)
                        adapter?.notifyItemRemoved(holder.adapterPosition)
                        //adapter?.notifyDataSetChanged()

                        //Reduce counter...
                        setTaskNumber()

                        holder.isDone.isChecked = false
                        holder.taskText.paintFlags = 0

                        recyclerView?.let {
                            Snackbar
                                .make(it, "Task has been deleted", Snackbar.LENGTH_SHORT)
                                /*.setAction("Undo")
                                {
                                    data?.add(holder.adapterPosition, deletedData)
                                    adapter?.notifyItemInserted(5)
                                    Snackbar
                                        .make(recyclerView!!, "Task has been restored", Snackbar.LENGTH_SHORT)
                                        .show()
                                }*/
                                .show()
                        }
                        /* To only enable removing of done tasks...
                        else {
                            //Need to re-add item to RecyclerView, because onSwiped deletes it
                            val position = holder.adapterPosition
                            val currData = data!![position]
                            data?.removeAt(position)
                            adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                            data?.add(position, currData)
                            adapter?.notifyItemInserted(position)
                            Snackbar
                                .make(recyclerView!!, "You can only remove tasks, that are done!", Snackbar.LENGTH_SHORT)
                                .show()

                        }
                         */
                    }
                    ItemTouchHelper.LEFT -> {
                        val position = holder.adapterPosition
                        val currData = data!![position]
                        data?.removeAt(position)

                        adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                        data?.add(position, currData)
                        adapter?.notifyItemInserted(position)

                        showAlertDialog(position, data!!)

                    }
                }

            }
        }

        val myHelper = ItemTouchHelper(myCallback)

        myHelper.attachToRecyclerView(recyclerView)

        floatingActionButton.setOnClickListener {
            //showAlertDialog(0, data!!)
            //val intent = Intent(this, TaskCreateActivity::class.java)
            val createTaskFragment = CreateTaskFragment(this)
            createTaskFragment.show(supportFragmentManager, "dialogFragment")

            //startActivity(intent)
        }

    }

    private fun updateLongTextMap(from: Int, to: Int) {
        Log.d("updateLongTextMap", "from: $from, to: $to")
        val temp = longTexts[from]
        longTexts[from] = longTexts[to]!!
        longTexts[to] = temp!!
    }

    private fun resetAdapter() {
        adapter = RecyclerViewAdapter(data!!, this)
        recyclerView?.adapter = adapter
        adapter?.notifyItemRangeChanged(0, data!!.size)
    }

    override fun onPause() {
        super.onPause()
        //Save data to Shared Pref
        //saveData()
        sharedPrefHandler?.saveData(SHARED_PREF_TASKS, SHARED_PREF_LTEXT, data!!)
    }

    private fun showAlertDialog(position: Int, data: ArrayList<Task>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(ALERT_TITLE)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.edit_text
        builder.setView(dialogLayout)
        val alertDialog = builder.create()
        builder.setPositiveButton("Add") { _, _ ->
            val task = editText.text.toString()
            addTask(task)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            alertDialog.cancel()
        }

        if (position >= 0 && !this.floatingActionButton.isPressed) {
            builder.create()
            builder.setTitle(RENAME_TITLE)
            val text = data[position].taskText
            editText.append(text)
            builder.setPositiveButton("Edit") { _, _ ->
                val task = editText.text.toString()
                data[position].taskText = task
                adapter?.notifyItemChanged(position)
                recyclerView?.let {
                    Snackbar
                        .make(it, "Task has been changed to $task", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
        builder.show()
    }

    private fun addTask(task: String) {
        if (task.isNotEmpty()) {
            data?.add(Task(task, false))
            adapter?.notifyItemInserted(data!!.size - 1)
            recyclerView?.smoothScrollToPosition(data!!.size - 1)
            setTaskNumber()
            longTexts[data!!.lastIndex] = ""
            recyclerView?.smoothScrollToPosition(data!!.size - 1)
        } else {
            Snackbar
                .make(recyclerView!!, "You can't add an empty task!", Snackbar.LENGTH_SHORT)
                .show()
        }


    }


    /*
    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        var json: String = gson.toJson(data)

        editor.putString(SHARED_PREF_TASKS, json)

        json = gson.toJson(longTexts)

        editor.putString(SHARED_PREF_LTEXT, json)

        editor.apply()
    }
    */

    /*
    private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        val gson = Gson()

        var json = sharedPreferences.getString(SHARED_PREF_TASKS, null)

        var type: Type = object : TypeToken<ArrayList<Task>>() {}.type

        data = gson.fromJson(json, type)

        if (data == null) {
            data = ArrayList()
        }

        json = sharedPreferences.getString(SHARED_PREF_LTEXT, null)

        type = object : TypeToken<HashMap<Int, String>>() {}.type

        val temp: HashMap<Int, String>? = gson.fromJson(json, type)
        if (temp != null) longTexts = temp
    }
*/
    @SuppressLint("SetTextI18n")
    private fun setTaskNumber() {
        taskNumber.text = "${adapter?.itemCount}"
    }

    private fun removeLongTexts(position: Int) {
        val temp: HashMap<Int, String> = HashMap()
        longTexts.remove(position)

        if (position == 0) {
            for (key in longTexts.keys) {
                val keyTemp = key - 1
                temp[keyTemp] = longTexts[key] as String
            }
        }

        for (key in longTexts.keys) {
            if (key < position) {
                temp[key] = longTexts[key] as String
                continue
            }
            val keyTemp = key - 1
            temp[keyTemp] = longTexts[key] as String
        }
        longTexts = temp
    }

    private fun generateTestData(startIndex: Int, endIndex: Int) {
        for (i in startIndex..endIndex) {
            data?.add(Task("Task $i", false))
            longTexts[i] = i.toString()
        }
        adapter?.notifyItemRangeInserted(startIndex, endIndex)
    }

    //OnClickListener for RecyclerView items
    override fun onCheckBoxClicked(position: Int, itemView: View) {
        //itemView.checkBox.isChecked = !itemView.checkBox.isChecked
        if (itemView.task_text.paintFlags != Paint.STRIKE_THRU_TEXT_FLAG)
            itemView.task_text.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        else
            itemView.task_text.paintFlags = 0
        if (itemView.checkBox.isChecked) {
            itemView.alpha = 0.4f
        } else {
            itemView.alpha = 1.0f
        }
        if (data?.getOrNull(position) != null) {
            data!![position].done = itemView.checkBox.isChecked
            data!![position].taskText = itemView.task_text.text.toString()
        }
    }

    override fun onCardClicked(position: Int, itemView: View) {
        val intent = Intent(this, LongTextActivity::class.java)
        intent.putExtra("task_name", data!![position].taskText)
        intent.putExtra("index", position)
        if (position in longTexts.keys) {
            intent.putExtra("long_text", longTexts[position])
        }
        startActivity(intent)
    }

    override fun onCreateTask(task: String) {
        addTask(task)
    }
}