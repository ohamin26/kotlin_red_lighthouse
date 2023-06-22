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
    var nameReceive:String? = null
    var addressReceive:String? = null
    var placeReviewDao = PlaceReviewDao()
    private lateinit var binding: ActivityLocationDetailsBinding
    var reviewList = arrayListOf<review>(
        review("logo","고수민","리뷰내용","2023-06-01"),
        review("logo","서진형","리뷰내용","2023-06-01"),
        review("logo","오하민","리뷰내용","2023-06-01"),
        review("logo","정세진","리뷰내용","2023-06-01"),
        review("logo","손하람","리뷰내용","2023-06-01"),
    )
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)
        var reviewList = arrayListOf<PlaceReview>()
        CoroutineScope(Dispatchers.Main).launch{
            var documents = placeReviewDao.getDataWithAddress(intent.getStringExtra("address").toString())
            for (document in documents){
                reviewList.add(document)
            }
        }

        title = findViewById(R.id.title)
        tv_attractionName = findViewById(R.id.tv_attractionName)
        address = findViewById(R.id.address)

        nameReceive = intent.getStringExtra("name")
        addressReceive = intent.getStringExtra("address")

        title.text = nameReceive
        tv_attractionName.text = nameReceive
        address.text = addressReceive

        setContentView(binding.root)

        val reviewAdapter = ReviewListAdapter(this, reviewList)
        binding.reviewList.adapter = reviewAdapter

        binding.btnBack.setOnClickListener{
            finish()
        }
    }
}