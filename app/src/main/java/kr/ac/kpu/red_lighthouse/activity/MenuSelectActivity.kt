package kr.ac.kpu.red_lighthouse.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.red_lighthouse.R

class MenuSelectActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        val bnvMain = findViewById<BottomNavigationView>(R.id.bnv_main)
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnvMain.run { setOnNavigationItemSelectedListener {
            // user의 nickname이 변경 되었을 시에는 바로 userInfoActivity로 이동
            if(intent.getBooleanExtra("userNicknameChange",false)){
                val fragment = UserInfoActivity()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
            }
            else{
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
            }
            true
        }
            selectedItemId = R.id.first
        }
    }
    //뒤로가기 막기
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}