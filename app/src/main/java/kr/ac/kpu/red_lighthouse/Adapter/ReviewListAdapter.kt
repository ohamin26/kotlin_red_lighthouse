package kr.ac.kpu.red_lighthouse.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.placeReview.review


class ReviewListAdapter (val context : Context, val reviewList : ArrayList<review>) : BaseAdapter(){
    @SuppressLint("MissingInflatedId", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.review_item, null)

        val userName = view.findViewById<TextView>(R.id.userName)
        val date = view.findViewById<TextView>(R.id.date)
        val reviewContent = view.findViewById<TextView>(R.id.review_content)


        val review = reviewList[position]
        userName.text = review.userName
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