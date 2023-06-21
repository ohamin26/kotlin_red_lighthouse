package kr.ac.kpu.red_lighthouse.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kr.ac.kpu.red_lighthouse.R

class LocationDetailsActivity : AppCompatActivity() {
    lateinit var title:TextView
    lateinit var tv_attractionName:TextView
    lateinit var address:TextView
    var nameReceive:String? = null
    var addressReceive:String? = null
    var indutypeNum:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
    }
}