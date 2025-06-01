package com.zrzring.douyin_memo.dao

import androidx.room.*
import com.zrzring.douyin_memo.model.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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