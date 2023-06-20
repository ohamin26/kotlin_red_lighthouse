package kr.ac.kpu.red_lighthouse.activity

import android.R.string
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.red_lighthouse.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class MenuSelectActivity : AppCompatActivity(){
    lateinit var btn_search : ImageButton
    lateinit var search_loc : EditText
    lateinit var list: ListView
    lateinit var listItem: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    var map = HashMap<String,ArrayList<String>>() // 검색한 장소 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        val intent = intent;
        var user_info_change = intent.getIntExtra("user_info_changed",1)

        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    //  프래그먼트 화면(MapActivity)으로 이동하는 기능
                    val fragment = MapActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                }
                R.id.second -> {
                    // 프래그먼트 화면(공유명소)으로 이동하는 기능
                    val fragment = LocationAddActivity() //임시 값 / 나중에 공유명소 화면으로 전환하도록 변경 예정
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                }
                R.id.third -> {
                    // 프래그먼트 화면(유저정보)으로 이동하는 기능
                    val fragment = UserInfoActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                }
            }
            true
        }
        }
    }


    override fun onBackPressed() {
        //super.onBackPressed()
    }
}