package kr.ac.kpu.red_lighthouse.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityChangeInfoBinding
import kr.ac.kpu.red_lighthouse.function.CheckUserId

class ChangeInfoActivity : AppCompatActivity(){
    private  lateinit var binding: ActivityChangeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnChange = binding.btnChange
        btnChange.setOnClickListener {
            var emailChange : String = binding.emailChange.text.toString()
            var nicknameChange : String = binding.nicknameChange.text.toString()
            if("" == emailChange){
                Toast.makeText(this@ChangeInfoActivity,"이메일을 입력하세요", Toast.LENGTH_SHORT).show()
                binding.emailChange.requestFocus()

            }else if(!CheckUserId().checkEmail(emailChange)) {
                Toast.makeText(this@ChangeInfoActivity, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT)
                    .show()
                binding.emailChange.requestFocus()

            }else if("" == nicknameChange){
                Toast.makeText(this@ChangeInfoActivity,"닉네임을 입력하세요", Toast.LENGTH_SHORT).show()
                binding.nicknameChange.requestFocus()

            }else{
                var intent = Intent(applicationContext, MenuSelectActivity::class.java)
                intent.putExtra("user_info_changed",0)
                startActivity(intent)
            }
        }
    }
}