package com.example.shangjifaxian

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shangjifaxian.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //提交商机按钮
        binding.btSend.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
            finish()
        }

        //查看商机按钮
        binding.btCheck.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
            finish()
        }

        //审核商机按钮
        binding.btExamine.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
            finish()
        }

        //变现商机按钮
        binding.btMaketrue.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}