package com.example.shangjifaxian


import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import com.example.shangjifaxian.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("data3", Context.MODE_PRIVATE)
        val isremember = prefs.getBoolean("rememberPass", false)
        if (isremember) {
            val username = prefs.getString("username", "")
            binding.accountEdit.setText(username)
            val password = prefs.getString("password", "")
            binding.passwordEdit.setText(password)
            binding.rememberPass.isChecked = true
        }

        binding.login.setOnClickListener {
            val username = binding.accountEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            if (username == "admin" && password == "123456") {
                val editor = getSharedPreferences("data3", Context.MODE_PRIVATE).edit()
                if (binding.rememberPass.isChecked == true) {
                    editor.putString("username", username)
                    editor.putString("password", password)
                    editor.putBoolean("rememberPass", true)
                } else editor.clear()
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else Toast.makeText(
                this, "account or password is invalid",
                Toast.LENGTH_SHORT
            ).show()
        }


    }


}