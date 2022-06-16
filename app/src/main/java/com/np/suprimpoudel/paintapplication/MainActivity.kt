package com.np.suprimpoudel.paintapplication

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.np.suprimpoudel.paintapplication.PaintView.Companion.colorList
import com.np.suprimpoudel.paintapplication.PaintView.Companion.currentBrush
import com.np.suprimpoudel.paintapplication.PaintView.Companion.pathList
import com.np.suprimpoudel.paintapplication.databinding.ActivityMainBinding
import java.io.OutputStream
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var path = Path()
        var paintBrush = Paint()
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initListener()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {
        when (view?.id) {
            binding.imvBlackStroke.id -> setBlackStroke()
            binding.imvGreenStroke.id -> setGreenStroke()
            binding.imvYellowStroke.id -> setYellowStroke()
            binding.imvRedStroke.id -> setRedStroke()
            binding.imvSaveIcon.id -> saveToDeviceStorage()
            binding.imvClearIcon.id -> clearScreen()
        }
    }

    private fun initListener() {
        binding.imvBlackStroke.setOnClickListener(this)
        binding.imvGreenStroke.setOnClickListener(this)
        binding.imvRedStroke.setOnClickListener(this)
        binding.imvYellowStroke.setOnClickListener(this)
        binding.imvSaveIcon.setOnClickListener(this)
        binding.imvClearIcon.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setBlackStroke() {
        paintBrush.color = getColor(R.color.black)
        setPathColor(paintBrush.color)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setGreenStroke() {
        paintBrush.color = getColor(R.color.green)
        setPathColor(paintBrush.color)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setRedStroke() {
        paintBrush.color = getColor(R.color.red)
        setPathColor(paintBrush.color)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setYellowStroke() {
        paintBrush.color = getColor(R.color.yellow)
        setPathColor(paintBrush.color)
    }

    private fun saveToDeviceStorage() {
        val b = convertViewToDrawable(binding.lltPaintArea)
        val imageOutStream: OutputStream?

        val cv = ContentValues()
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "${UUID.randomUUID()}.png")
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        try {
            imageOutStream = uri?.let { contentResolver.openOutputStream(it) }
            b.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)
            imageOutStream?.close()
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearScreen() {
        pathList.clear()
        colorList.clear()
        path.reset()
    }

    private fun setPathColor(colorResource: Int) {
        currentBrush = colorResource
        path = Path()
    }

    private fun convertViewToDrawable(view: View): Bitmap {
        val b = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        c.translate((-view.scrollX).toFloat(), (-view.scrollY).toFloat())
        view.draw(c)
        return b
    }
}