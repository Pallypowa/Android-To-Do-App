package com.example.todoapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context): SQLiteOpenHelper(context, "todoapp.db", null, 1) {

    object TaskTable{
        const val TASK_TABLE = "TASK_TABLE"
        const val ID = "ID"
        const val TASK_TEXT = "TASK_TEXT"
        const val DONE = "DONE"
        const val DEADLINE = "DEADLINE"
        const val CATID = "CATID"
        const val LONGTEXT = "LONGTEXT"
    }

    object CategoryTable{
        const val CATEGORY_TABLE = "CATEGORY_TABLE"
        const val ID = "ID"
        const val CATTEXT = "CATEGORY_TEXT"
        const val ITEMNUM = "ITEMNUM"
        const val COLOR = "COLOR"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTaskTable = "CREATE TABLE ${TaskTable.TASK_TABLE} " +
                "(${TaskTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${TaskTable.TASK_TEXT} TEXT, " +
                "${TaskTable.DONE} BOOL, " +
                "${TaskTable.DEADLINE} DATE, " +
                "${TaskTable.CATID} INTEGER, " +
                "${TaskTable.LONGTEXT} TEXT)"


        val createCategoryTable = "CREATE TABLE ${CategoryTable.CATEGORY_TABLE} " +
                "(${CategoryTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${CategoryTable.CATTEXT} TEXT, " +
                "${CategoryTable.ITEMNUM} INTEGER, " +
                "${CategoryTable.COLOR} TEXT)"

        db!!.execSQL(createTaskTable)
        db.execSQL(createCategoryTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun addTask(task: Task): Boolean{
        val contentValues = ContentValues()
        val db = this.writableDatabase

        contentValues.put(TaskTable.TASK_TEXT, task.taskText)
        contentValues.put(TaskTable.DONE, task.done)
        contentValues.put(TaskTable.DEADLINE, task.deadline)
        contentValues.put(TaskTable.CATID, task.categoryId)
        contentValues.put(TaskTable.LONGTEXT, task.longText)

        return !db.insert(TaskTable.TASK_TABLE, null, contentValues).equals(-1)
    }

    fun addCategory(category: Category): Boolean{
        val contentValues = ContentValues()
        val db = this.writableDatabase

        contentValues.put(CategoryTable.CATTEXT, category.categoryName)
        contentValues.put(CategoryTable.ITEMNUM, category.taskCount)
        contentValues.put(CategoryTable.COLOR, category.color)

        return !db.insert(CategoryTable.CATEGORY_TABLE, null, contentValues).equals(-1)
    }

    fun readTasks(categoryId: Int): ArrayList<Task>{
        val tasks = ArrayList<Task>()
        val queryString = "SELECT * FROM ${TaskTable.TASK_TABLE} WHERE ${TaskTable.CATID} = $categoryId"
        val db = this.readableDatabase

        val cursor = db.rawQuery(queryString, null)
        if(cursor.moveToFirst()){
            do {
                val taskText: String = cursor.getString(1)
                var done = true
                if (cursor.getInt(2) == 0){
                    done = false
                }
                val date = cursor.getString(3)
                val lText = cursor.getString(5)
                tasks.add(Task(taskText, done as Boolean, date, 0, lText))
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return tasks
    }
}