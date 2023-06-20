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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        val intent = intent;
        var user_info_change = intent.getBooleanExtra("user_info_changed",false)


        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            if(user_info_change){
                val fragment = UserInfoActivity()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                user_info_change = false
            }else{
                when(it.itemId) {
                    R.id.first -> {
                        //  프래그먼트 화면(MapActivity)으로 이동하는 기능
                        val fragment = MapActivity()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                    }
                    R.id.second -> {
                        // 프래그먼트 화면(LocationListActivity)으로 이동하는 기능
                        val fragment = LocationListActivity() //임시 값 / 나중에 공유명소 화면으로 전환하도록 변경 예정
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                    }
                    R.id.third -> {
                        // 프래그먼트 화면(UserInfoActivity)으로 이동하는 기능
                        val fragment = UserInfoActivity()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                    }
                }
            }
            true
            }
            selectedItemId = R.id.first
        }
    }
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}