package com.zrzring.douyin_memo.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zrzring.douyin_memo.model.Memo

class MemoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "memos.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_MEMOS = "memos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSql = "CREATE TABLE $TABLE_MEMOS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MEMOS")
        onCreate(db)
    }

    // --- CRUD Operations ---

    fun addMemo(memo: Memo): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, memo.title)
            put(COLUMN_CONTENT, memo.content)
        }
        val id = db.insert(TABLE_MEMOS, null, values)
        db.close()
        return id
    }

    fun getAllMemos(): List<Memo> {
        val memoList = mutableListOf<Memo>()
        val selectQuery = "SELECT * FROM $TABLE_MEMOS ORDER BY $COLUMN_ID DESC"
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val memo = Memo(
                        id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                        content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                    )
                    memoList.add(memo)
                } while (it.moveToNext())
            }
        }
        db.close()
        return memoList
    }

    fun updateMemo(memo: Memo): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, memo.title)
            put(COLUMN_CONTENT, memo.content)
        }
        val result = db.update(TABLE_MEMOS, values, "$COLUMN_ID = ?", arrayOf(memo.id.toString()))
        db.close()
        return result
    }

    fun deleteMemo(memoId: Long): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_MEMOS, "$COLUMN_ID = ?", arrayOf(memoId.toString()))
        db.close()
        return result
    }
}