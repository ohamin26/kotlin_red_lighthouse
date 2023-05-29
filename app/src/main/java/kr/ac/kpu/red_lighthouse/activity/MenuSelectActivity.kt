package kr.ac.kpu.red_lighthouse.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.red_lighthouse.R

class MenuSelectActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        var bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        val intent = intent;
        var user_info_change = intent.getIntExtra("user_info_changed",1)
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {

            if(user_info_change === 0){
                // 회원 정보 수정 시에 유저정보 프레그먼트 화면(UserInfoActivity)으로 이동한다.
                val fragment = UserInfoActivity()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                user_info_change = 1
            }else{
                when(it.itemId) {
                    R.id.first -> {
                        //  프래그먼트 화면(MapActivity)으로 이동하는 기능
                        val fragment = MapActivity()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                    }
                    R.id.second -> {
                        // 프래그먼트 화면(공유명소)으로 이동하는 기능
                        val fragment = UserInfoActivity() //임시 값 / 나중에 공유명소 화면으로 전환하도록 변경 예정
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
}