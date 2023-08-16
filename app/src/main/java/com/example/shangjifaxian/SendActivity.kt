package com.example.shangjifaxian

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shangjifaxian.databinding.ActivitySendBinding

class SendActivity : AppCompatActivity() {

    private lateinit var sp_dropdown : Spinner

    //定义分局下拉列表
    private val starArray =
        arrayOf("校园分局", "文宣分局", "电子科大分局", "420分局", "东郊记忆分局", "东客站分局", "二十四城分局", "二仙桥分局")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp_dropdown = binding.spDropdown

        //声明一个下拉列表的数组适配器// 第一个参数：上下文，第二个参数：条目布局，第三个参数：要显示的数据
        val startAdapter: ArrayAdapter<String> =ArrayAdapter(this, R.layout.item_select, starArray)
        //将适配器给Spinner
        sp_dropdown.setAdapter(startAdapter)
        //设置下拉框默认显示第一项
        sp_dropdown.setSelection(0)
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
       // sp_dropdown.setOnItemSelectedListener(this)

    }


}