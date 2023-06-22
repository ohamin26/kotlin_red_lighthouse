package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kpu.red_lighthouse.Adapter.MyReviewListAdapter
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
    var indutype_num:String? = null
    var placeReviewDao = PlaceReviewDao()
    private lateinit var binding: ActivityLocationDetailsBinding
    var reviewList = arrayListOf<PlaceReview>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
//        val prefs = getSharedPreferences("user",0)

        CoroutineScope(Dispatchers.Main).launch{
            var documents = placeReviewDao.getDataWithAddress(intent.getStringExtra("address").toString())
            for (document in documents){
                reviewList.add(document)
            }
            Log.e("파이어베이스",documents[0].uid)
            adapter(reviewList)
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
        indutype_num = intent.getStringExtra("indutype_num")

        title.text = nameReceive
        tv_attractionName.text = nameReceive
        address.text = addressReceive
        sort.text = indutype_num
        setContentView(binding.root)


        binding.btnBack.setOnClickListener{
            finish()
        }
    }
    fun adapter(reviewList:ArrayList<PlaceReview>){
        val reviewAdapter = ReviewListAdapter(this, reviewList)
        binding.reviewList.adapter = reviewAdapter
    }
}