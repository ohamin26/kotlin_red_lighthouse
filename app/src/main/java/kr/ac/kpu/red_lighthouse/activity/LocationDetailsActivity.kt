package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ListView
import kr.ac.kpu.red_lighthouse.Adapter.ReviewListAdapter
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityLocationDetailsBinding
import kr.ac.kpu.red_lighthouse.placeReview.myReview
import kr.ac.kpu.red_lighthouse.placeReview.review

class LocationDetailsActivity : AppCompatActivity() {
    lateinit var title:TextView
    lateinit var tv_attractionName:TextView
    lateinit var address:TextView
    var nameReceive:String? = null
    var addressReceive:String? = null
    var indutypeNum:String? = null

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

        title = findViewById(R.id.title)
        tv_attractionName = findViewById(R.id.tv_attractionName)
        address = findViewById(R.id.address)

        nameReceive = intent.getStringExtra("name")
        addressReceive = intent.getStringExtra("address")
        indutypeNum = intent.getStringExtra("indutype_num")

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