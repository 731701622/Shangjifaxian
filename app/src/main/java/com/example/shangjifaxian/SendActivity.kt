package com.example.shangjifaxian

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.shangjifaxian.databinding.ActivitySendBinding
import java.io.File
import java.util.Calendar
import java.util.TimeZone

class SendActivity : AppCompatActivity() {
    //获取滑动条
    private lateinit var sp_dropdown: Spinner

    //获取日期
    private lateinit var cal: Calendar
    private lateinit var year: String
    private lateinit var month: String
    private lateinit var day: String
    private lateinit var hour: String
    private lateinit var minute: String

    //获取日期输入条
    private lateinit var et_year: EditText
    private lateinit var et_month: EditText
    private lateinit var et_day: EditText
    private lateinit var et_hour: EditText
    private lateinit var et_minute: EditText

    //获取照片及按钮
    private lateinit var photo: ImageView
    private lateinit var bt_takephoto: Button
    private lateinit var bt_fromAlbumBtn: Button
    private val takePhoto = 1
    private val fromAlbum = 2
    private lateinit var imageUri: Uri
    private lateinit var outputImage: File


    //定义分局下拉列表
    private val starArray =
        arrayOf(
            "校园分局",
            "文宣分局",
            "电子科大分局",
            "420分局",
            "东郊记忆分局",
            "东客站分局",
            "二十四城分局",
            "二仙桥分局"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp_dropdown = binding.spDropdown
        et_year = binding.etYear
        et_month = binding.etMonth
        et_day = binding.etDay
        et_hour = binding.etHour
        et_minute = binding.etMinute

        setcurrentTime()
        initDropdown()
        initcurrentTime()

        bt_takephoto = binding.btTakephoto
        bt_fromAlbumBtn = binding.btFromAlbum
        photo = binding.photo

        // 拍照按钮
        bt_takephoto.setOnClickListener {
            // 创建File对象，用于存储拍照后的图片
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    this, "com.example.shangjifaxian.fileprovider", outputImage
                )
            } else {
                Uri.fromFile(outputImage)
            }
            // 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
            if(photo.visibility == View.GONE)
            photo.visibility = View.VISIBLE
        }

        // 从相册中选择按钮
        bt_fromAlbumBtn.setOnClickListener {
            // 打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示图片
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)
            if(photo.visibility == View.GONE)
                photo.visibility = View.VISIBLE
        }
    }


    /*给日期默认初始化成当前时间*/
    private fun initcurrentTime() {
        et_year.setText(year)
        et_month.setText(month)
        et_day.setText(day)
        et_hour.setText(hour)
        et_minute.setText(minute)
    }

    /*初始化滑动条*/
    private fun initDropdown() {
        //声明一个下拉列表的数组适配器// 第一个参数：上下文，第二个参数：条目布局，第三个参数：要显示的数据
        val startAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.item_select, starArray)
        //将适配器给Spinner
        sp_dropdown.setAdapter(startAdapter)
        //设置下拉框默认显示第一项
        sp_dropdown.setSelection(0)
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        // sp_dropdown.setOnItemSelectedListener(this)
    }

    /*获取当前日期，并赋初值*/
    private fun setcurrentTime() {
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()
        day = cal.get(Calendar.DATE).toString()
        if (cal.get(Calendar.AM_PM) == 0)
            hour = cal.get(Calendar.HOUR).toString()
        else
            hour = (cal.get(Calendar.HOUR) + 12).toString()
        minute = cal.get(Calendar.MINUTE).toString()

        //Log.d("SendActivity", "setcurrentTime:$year 年 $month 月 $day 日 $hour 时 $minute 分 ")
    }

    /*手机拍照实现*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    val bitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(imageUri)
                    )
                    photo.setImageBitmap(rotateIfRequired(bitmap))
                }
            }

            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的图片显示
                        val bitmap = getBitmapFromUri(uri)
                        photo.setImageBitmap(bitmap)
                    }
                }

            }
        }
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height,
            matrix, true
        )
        bitmap.recycle() // 将不再需要的Bitmap对象回收
        return rotatedBitmap
    }
    /*手机拍照实现*/

    /*从相册选择照片实现*/
    private fun getBitmapFromUri(uri: Uri) = contentResolver
        .openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }
    /*从相册选择照片实现*/
}
