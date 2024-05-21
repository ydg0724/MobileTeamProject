package com.example.mobileteamproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MemoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "memo.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "memos"
        const val COLUMN_DATE = "date"
        const val COLUMN_MEMO = "memo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_DATE TEXT PRIMARY KEY, " +
                "$COLUMN_MEMO TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addOrUpdateMemo(date: String, memo: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_DATE, date)
        contentValues.put(COLUMN_MEMO, memo)
        db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
    }

    fun getMemo(date: String): String? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_MEMO),
            "$COLUMN_DATE=?", arrayOf(date),
            null, null, null
        )
        var memo: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_MEMO)
            if (columnIndex != -1) {
                memo = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        db.close()
        return memo
    }

    fun getAllMemos(): List<Memo> {
        val memos = mutableListOf<Memo>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_DATE ASC")

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val memo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMO))
                memos.add(Memo(date, memo))
            }
            cursor.close()
        }
        db.close()
        return memos
    }

    fun deleteMemo(date: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_DATE=?", arrayOf(date))
        db.close()
    }
}
