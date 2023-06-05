package kr.ac.kpu.red_lighthouse.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import kr.ac.kpu.red_lighthouse.databinding.ActivityChangeInfoNicknameBinding
import kr.ac.kpu.red_lighthouse.function.CheckUserId
import kr.ac.kpu.red_lighthouse.user.UserDao

class ChangeInfoNicknameActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityChangeInfoNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoNicknameBinding.inflate(layoutInflater)
        var userDao = UserDao()
        val prefs = getSharedPreferences("user", 0)
        val edit = prefs.edit()
        val userId = prefs.getString("userId"," ").toString()
        setContentView(binding.root)
        val btnChange = binding.btnChange

        //뒤로가기
        binding.btnBack.setOnClickListener{
            finish()
        }

        //닉네임 변경 이벤트
        btnChange.setOnClickListener {
            // 공백 체크
            val nicknameChange : String = binding.nicknameChange.text.toString()
            if("" == nicknameChange){
                Toast.makeText(this@ChangeInfoNicknameActivity,"닉네임을 입력하세요", Toast.LENGTH_SHORT).show()
                binding.nicknameChange.requestFocus()
            // 공백이 아닐 시 닉네임 변경, 화면 이동
            }else{
                userDao.updateNickname(userId, nicknameChange)
                edit.putString("userNickname",nicknameChange)
                edit.apply()
                var intent = Intent(applicationContext, ChangeInfoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}