package kr.ac.kpu.red_lighthouse.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityAppInfoBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityCsBinding

class AppInfoActivity :AppCompatActivity(){

    private lateinit var binding: ActivityAppInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}