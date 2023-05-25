package kr.ac.kpu.red_lighthouse.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityFindpwBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityLocationAddBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityMainBinding

class LocationAddActivity : AppCompatActivity() {
    // 인텐트 갤러리타입은 1
    private val GALLERY = 1
    private  lateinit var binding: ActivityLocationAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryBtn.setOnClickListener{
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,GALLERY)

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == Activity.RESULT_OK){
            if( requestCode ==  GALLERY)
            {
                var ImageData: Uri? = data?.data
                Toast.makeText(this,ImageData.toString(), Toast.LENGTH_SHORT ).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImageData)
                    binding.galleryView.setImageBitmap(bitmap)
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }
}