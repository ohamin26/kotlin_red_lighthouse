package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityAddReviewBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityLoginBinding
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReview
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReviewDao
import java.time.LocalDate

class AddReviewActivity : AppCompatActivity() {
    private val GALLERY = 1
    private lateinit var binding: ActivityAddReviewBinding
    private var cntImg : Int = 0
    var address:String? = null
    var isCheck = false
    var placeReviewDao = PlaceReviewDao()
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        address = intent.getStringExtra("address")
        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        }
        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.radioGroup.setOnCheckedChangeListener{
         group,checkId ->
            when(checkId){
                R.id.rdo_local_yes -> isCheck = true
                R.id.rdo_local_no -> isCheck = false
            }
        }
        binding.btnAdd.setOnClickListener{
            val onlyDate: LocalDate = LocalDate.now()
            //var placeReview = PlaceReview(address,binding.editPrice.text.toString(),isCheck,,binding.editReview.text.toString(),onlyDate.toString())
            //placeReviewDao.setDataToFirebase(placeReview)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                val imageData: Uri? = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver,//requireActivity().contentResolver
                        imageData
                    )
                    // 이미지뷰를 생성합니다.
                    if(cntImg < 3) {
                        val imageView = ImageView(applicationContext)
                        var displayMetrics = resources.displayMetrics
                        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        imageView.setImageBitmap(bitmap)
                        val imageLayoutParams = LinearLayout.LayoutParams(
                            Math.round(200 * displayMetrics.density),
                            Math.round(150 * displayMetrics.density)
                        )
                        imageLayoutParams.leftMargin = Math.round(10 * displayMetrics.density)
                        imageView.setLayoutParams(imageLayoutParams)

                        binding.layoutImagelist.addView(imageView)
                        cntImg += 1
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}