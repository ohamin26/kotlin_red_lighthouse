package kr.ac.kpu.red_lighthouse.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.red_lighthouse.R

class MenuSelectActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        var bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)

        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    val fragment = MapActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                }
                R.id.second -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    val fragment = UserInfoActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.first
        }
    }
}