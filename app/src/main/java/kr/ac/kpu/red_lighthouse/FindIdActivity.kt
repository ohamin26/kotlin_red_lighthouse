package kr.ac.kpu.red_lighthouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.red_lighthouse.databinding.ActivityFindidBinding

class FindIdActivity : Activity(){

    private  lateinit var binding: ActivityFindidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inputId = binding.userId
        val checkIdBtn = binding.checkIdBtn
        val newPwBtn = binding.newPwBtn
        val newPw = binding.findPw
        run {
            binding.goLogin.setOnClickListener {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        // 아이디 버튼 이벤트
        // 아이디 형식과 다르거나 데이터에 없는 아이디일 경우 경우 다시 입력하게 한다
        // 맞을 경우 새로운 비밀번호 생성 화면 표시
        checkIdBtn.setOnClickListener {
            Firebase.auth.sendPasswordResetEmail(binding.userId.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase", "Email sent.")
                        Toast.makeText(applicationContext, "이메일 전송 성공. 받은 메일 안에 링크를 통해 비밀번호를 변경하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}