package kr.ac.kpu.red_lighthouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kr.ac.kpu.red_lighthouse.databinding.ActivityLoginBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityMapBinding

class MapActivity : Activity(){

    private  lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val logoutBtn = binding.logoutBtn
        logoutBtn.setOnClickListener {
            PreferenceUse.prefs.setString("id", "")
            var intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}