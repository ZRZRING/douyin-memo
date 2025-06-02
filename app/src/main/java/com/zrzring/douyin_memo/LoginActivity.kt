package com.zrzring.douyin_memo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zrzring.douyin_memo.databinding.ActivityLoginBinding
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val prefsName = "LoginPrefs"
    private val keyRememberMe = "key.rememberme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean(keyRememberMe, true)) {
            navigateToMemoList()
            return
        }

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (binding.checkBoxRememberMe.isChecked) {
                    sharedPreferences.edit {
                        putBoolean(keyRememberMe, true)
                    }
                } else {
                    sharedPreferences.edit {
                        putBoolean(keyRememberMe, false)
                    }
                }
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                navigateToMemoList()
            } else {
                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMemoList() {
        val intent = Intent(this, MemoListActivity::class.java)
        startActivity(intent)
        finish()
    }
}