package kr.ac.kpu.red_lighthouse.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.placeReview.myReview

class MyReviewListAdapter (val context : Context, val reviewList : ArrayList<myReview>) : BaseAdapter(){
    @SuppressLint("MissingInflatedId", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.my_review_item, null)

        val placeName = view.findViewById<TextView>(R.id.placeName)
        val date = view.findViewById<TextView>(R.id.date)
        val reviewContent = view.findViewById<TextView>(R.id.review_content)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val review = reviewList[position]
        placeName.text = review.placeName
        date.text = review.date
        reviewContent.text = review.reviewData

        return view
    }

    override fun getItem(position: Int): Any {
        return reviewList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return reviewList.size
    }
}