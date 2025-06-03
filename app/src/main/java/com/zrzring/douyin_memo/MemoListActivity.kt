package com.zrzring.douyin_memo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrzring.douyin_memo.adapter.MemoAdapter
import com.zrzring.douyin_memo.databinding.ActivityMemoListBinding
import com.zrzring.douyin_memo.db.MemoDbHelper
import androidx.core.content.edit


class MemoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemoListBinding
    private lateinit var dbHelper: MemoDbHelper
    private lateinit var memoAdapter: MemoAdapter

    private fun loadMemos() {
        val memos = dbHelper.getAllMemos()
        memoAdapter.updateData(memos)
    }

    // 注册回调
    private val detailActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loadMemos()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 绑定布局，将 Toolbar设置为顶部应用栏
        binding = ActivityMemoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // 创建数据库操作
        dbHelper = MemoDbHelper(this)

        // 配置列表
        setupRecyclerView()

        // 监听点击具体一条备忘录事件，跳转对应 detail 界面
        binding.fabAddMemo.setOnClickListener {
            val intent = Intent(this, MemoDetailActivity::class.java)
            detailActivityLauncher.launch(intent)
        }

        loadMemos()
    }

    // 挂载退出登录组件
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.memo_menu, menu)
        return true
    }

    // 设置退出登录组件按钮逻辑
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
                sharedPreferences.edit {
                    putBoolean(LoginActivity.KEY_REMEMBER_ME, false)
                }
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        // 点击 item 跳转到详情页
        memoAdapter = MemoAdapter(emptyList()) { memo ->
            val intent = Intent(this, MemoDetailActivity::class.java).apply {
                putExtra("MEMO_ID", memo.id)
                putExtra("MEMO_TITLE", memo.title)
                putExtra("MEMO_CONTENT", memo.content)
            }
            detailActivityLauncher.launch(intent)
        }

        // 绑定适配器
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MemoListActivity)
            adapter = memoAdapter
        }
    }
}