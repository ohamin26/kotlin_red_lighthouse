package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kpu.red_lighthouse.Adapter.MyReviewListAdapter
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityMyReviewBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityPolicyBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityUserInfoBinding
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReview
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReviewDao
import kr.ac.kpu.red_lighthouse.placeReview.myReview

class MyReviewActivity() : AppCompatActivity(){
    private lateinit var binding: ActivityMyReviewBinding
    //데이터 입력 - 현재 임시 데이터 값 넣어놨습니다.
    //데이터 추가 시 myReview("명소 이름", "내용", "날짜") 순으로 입력
    private var reviewList = arrayListOf<myReview>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?){
        val prefs = getSharedPreferences("user", 0)

        CoroutineScope(Dispatchers.Main).launch {
            val reviewDao = PlaceReviewDao()
            var review : PlaceReview? = PlaceReview()

            if (review != null) {
                review = reviewDao.getDataFromFirebase(review.uid)
                while (review != null) {
                    reviewList.add(myReview(review.placeName,review.review,review.dateOfReview))
                }
            }
        }
        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //이메일, 닉네임 표시
        binding.userEmail.text = prefs.getString("userEmail","").toString()
        binding.userName.text = prefs.getString("userNickname","").toString()


        val reviewAdapter = MyReviewListAdapter(this, reviewList)
        binding.reviewListView.adapter = reviewAdapter

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}

