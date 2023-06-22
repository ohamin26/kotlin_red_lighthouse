package kr.ac.kpu.red_lighthouse.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReview
import kr.ac.kpu.red_lighthouse.placeReview.review
import kr.ac.kpu.red_lighthouse.user.User
import kr.ac.kpu.red_lighthouse.user.UserDao
import java.net.URL


class ReviewListAdapter(val context: Context, val reviewList: ArrayList<PlaceReview>) : BaseAdapter(){
    @SuppressLint("MissingInflatedId", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.review_item, null)

        val reviewImage = view.findViewById<ImageView>(R.id.reviewImage)
        val uid = view.findViewById<TextView>(R.id.userName)
        val date = view.findViewById<TextView>(R.id.date)
        val reviewContent = view.findViewById<TextView>(R.id.review_content)
        val isLocalCurrency = view.findViewById<TextView>(R.id.isLocalCurrency)
        val placePrice = view.findViewById<TextView>(R.id.placePrice)
        val userDao = UserDao()
        val review = reviewList[position]
        var users : User?
        CoroutineScope(Dispatchers.Main).launch{
            users = userDao.getDataFromFirebase(review.uid)
            uid.text = users?.userNickname
        }
        date.text = review.dateOfReview
        reviewContent.text = review.review
        isLocalCurrency.text = review.isLocalCurrency.toString()
        placePrice.text = review.placePrice
        if(review.placePhotos1 != null && review.placePhotos1 != ""){
            CoroutineScope(Dispatchers.IO).launch {
                val url = URL(review.placePhotos1)
                val stream = url.openStream()
                val bitmap = BitmapFactory.decodeStream(stream)
                launch(Dispatchers.Main) {
                    reviewImage.setImageBitmap(bitmap)
                }

            }
        }

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