package com.zrzring.douyin_memo

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zrzring.douyin_memo.databinding.ActivityMemoDetailBinding
import com.zrzring.douyin_memo.db.MemoDbHelper
import com.zrzring.douyin_memo.model.Memo

class MemoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemoDetailBinding
    private lateinit var dbHelper: MemoDbHelper
    private var currentMemo: Memo? = null

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MemoDbHelper(this)

        val memoId = intent.getLongExtra("MEMO_ID", -1L)

        if (memoId != -1L) {
            title = "编辑备忘录"
            val title = intent.getStringExtra("MEMO_TITLE")
            val content = intent.getStringExtra("MEMO_CONTENT")
            currentMemo = Memo(memoId, title ?: "", content ?: "")
            binding.editTextMemoTitle.setText(currentMemo?.title)
            binding.editTextMemoContent.setText(currentMemo?.content)
            binding.buttonDelete.visibility = View.VISIBLE
        } else {
            title = "新建备忘录"
            binding.buttonDelete.visibility = View.GONE
        }

        binding.buttonSave.setOnClickListener {
            saveMemo()
        }

        binding.buttonDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun saveMemo() {
        val title = binding.editTextMemoTitle.text.toString().trim()
        val content = binding.editTextMemoContent.text.toString().trim()

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "标题和内容不能都为空", Toast.LENGTH_SHORT).show()
            return
        }

        val finalTitle = title.ifEmpty { "无标题" }

        if (currentMemo == null) {
            val memo = Memo(title = finalTitle, content = content)
            dbHelper.addMemo(memo)
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
        } else {
            currentMemo?.let {
                it.title = finalTitle
                it.content = content
                dbHelper.updateMemo(it)
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show()
            }
        }
        setResult(RESULT_OK)
        finish()
        overrideActivityTransition(
            OVERRIDE_TRANSITION_CLOSE,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除这条备忘录吗？此操作无法撤销。")
            .setPositiveButton("删除") { _, _ ->
                deleteMemo()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun deleteMemo() {
        currentMemo?.let {
            dbHelper.deleteMemo(it.id)
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}