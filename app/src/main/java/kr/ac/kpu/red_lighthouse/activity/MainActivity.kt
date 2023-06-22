package kr.ac.kpu.red_lighthouse.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import kr.ac.kpu.red_lighthouse.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            FirebaseApp.initializeApp(/*context=*/this)
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }catch (e : Exception){

        }

        val sharedPreference = getSharedPreferences("user", 0)

        // 자동 로그인 이벤트 - 자동 로그인 체크한 유저는 유저 정보가 있을 시 메뉴 화면으로 이동한다.
        if(sharedPreference.getBoolean("autoLogin", false)){
            if(sharedPreference != null){
                val intent = Intent(applicationContext, MenuSelectActivity::class.java)
                startActivity(intent)
            }
        }else{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        setContentView(R.layout.activity_main)
    }
}