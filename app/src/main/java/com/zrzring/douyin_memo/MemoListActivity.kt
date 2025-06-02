package com.zrzring.douyin_memo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrzring.douyin_memo.adapter.MemoAdapter
import com.zrzring.douyin_memo.databinding.ActivityMemoListBinding
import com.zrzring.douyin_memo.db.MemoDbHelper
import com.zrzring.douyin_memo.model.Memo

class MemoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemoListBinding
    private lateinit var dbHelper: MemoDbHelper
    private lateinit var memoAdapter: MemoAdapter

    // 使用新的 Activity Result API 来处理从详情页返回的结果
    private val detailActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 如果详情页有数据变动（保存或删除），则刷新列表
            loadMemos()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        dbHelper = MemoDbHelper(this)
        setupRecyclerView()

        binding.fabAddMemo.setOnClickListener {
            val intent = Intent(this, MemoDetailActivity::class.java)
            detailActivityLauncher.launch(intent)
        }

        loadMemos()
    }

    private fun setupRecyclerView() {
        memoAdapter = MemoAdapter(emptyList()) { memo ->
            // 点击 item 跳转到详情页
            val intent = Intent(this, MemoDetailActivity::class.java).apply {
                putExtra("MEMO_ID", memo.id)
                putExtra("MEMO_TITLE", memo.title)
                putExtra("MEMO_CONTENT", memo.content)
            }
            detailActivityLauncher.launch(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MemoListActivity)
            adapter = memoAdapter
        }
    }

    private fun loadMemos() {
        val memos = dbHelper.getAllMemos()
        memoAdapter.updateData(memos)
    }
}