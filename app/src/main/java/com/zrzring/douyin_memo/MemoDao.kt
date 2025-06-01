package com.zrzring.douyin_memo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(memo: Memo)

    @Update
    suspend fun update(memo: Memo)

    @Delete
    suspend fun delete(memo: Memo)

    @Query("SELECT * FROM memo ORDER BY updated_at DESC")
    fun getAllMemos(): Flow<List<Memo>>

    @Query("SELECT * FROM memo WHERE id = :memoId")
    suspend fun getMemoById(memoId: Int): Memo?
}