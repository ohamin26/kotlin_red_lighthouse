package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kpu.red_lighthouse.Adapter.ReviewListAdapter
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityLocationDetailsBinding
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReview
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReviewDao
import kr.ac.kpu.red_lighthouse.placeReview.review

class LocationDetailsActivity : AppCompatActivity() {
    lateinit var title:TextView
    lateinit var tv_attractionName:TextView
    lateinit var address:TextView
    lateinit var sort:TextView
    var nameReceive:String? = null
    var addressReceive:String? = null
    var placeReviewDao = PlaceReviewDao()
    private lateinit var binding: ActivityLocationDetailsBinding
    private var reviewList = arrayListOf<review>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("user",0)

        CoroutineScope(Dispatchers.Main).launch{
            val reviewDao = PlaceReviewDao()
            var review1 : PlaceReview? = PlaceReview()
            if(review1 != null){
                review1 = reviewDao.getDataFromFirebase(review1.address)
                while(review1 != null){
                    reviewList.add(review(review1.placePhotos1,review1.uid,review1.review,review1.dateOfReview,review1.isLocalCurrency.toString(),review1.placePrice))
                }
            }
//            var documents = placeReviewDao.getDataWithAddress(intent.getStringExtra("address").toString())
//            for (document in documents){
//                reviewList.add(document)
//            }
        }

        binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        title = findViewById(R.id.title)
        tv_attractionName = findViewById(R.id.tv_attractionName)
        address = findViewById(R.id.address)
        sort = findViewById(R.id.sort)

        nameReceive = intent.getStringExtra("name")
        addressReceive = intent.getStringExtra("address")

        title.text = nameReceive
        tv_attractionName.text = nameReceive
        address.text = addressReceive
        var reviewList = arrayListOf<PlaceReview>()
        setContentView(binding.root)
        val reviewAdapter = ReviewListAdapter(this, reviewList)
        binding.reviewList.adapter = reviewAdapter

        binding.btnBack.setOnClickListener{
            finish()
        }
    }
}