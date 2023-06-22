package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityAddReviewBinding
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
    var uriArr: Uri? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            FirebaseApp.initializeApp(/*context=*/this)
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }catch (e : Exception){

        }
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
            var imageArr: String? = ""
            CoroutineScope(Dispatchers.Main).launch{
                if (uriArr!= null) {
                    imageArr = placeReviewDao.uploadPhoto(uriArr!!, "img")
                }
                Log.e("파이어베이스", "${imageArr}")
                val sharedPreference = getSharedPreferences("user", 0)
                val uid = sharedPreference.getString("userId", "")
                print("----------------------------------------------------")
                var placeReview = PlaceReview(
                    address!!,
                    uid!!,
                    binding.editPrice.text.toString(),
                    isCheck,
                    imageArr.toString(),
                    binding.editReview.text.toString(),
                    onlyDate.toString()
                )
                placeReviewDao.setDataToFirebase(placeReview)
            }
            val intent = Intent(applicationContext, MenuSelectActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
            return
        }

        if (requestCode == GALLERY) {
            val imageData: Uri? = data?.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    contentResolver,//requireActivity().contentResolver
                    imageData
                )
                // 이미지뷰를 생성합니다.
                if(cntImg < 1) {
                    val imageView = ImageView(applicationContext)
                    var displayMetrics = resources.displayMetrics
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    imageView.setImageBitmap(bitmap)
                    val imageLayoutParams = LinearLayout.LayoutParams(
                        Math.round(200 * displayMetrics.density),
                        Math.round(150 * displayMetrics.density)
                    )
                    imageLayoutParams.leftMargin = Math.round(10 * displayMetrics.density)
                    imageView.layoutParams = imageLayoutParams
                    uriArr = data?.data
                    binding.layoutImagelist.addView(imageView)
                    cntImg += 1
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}