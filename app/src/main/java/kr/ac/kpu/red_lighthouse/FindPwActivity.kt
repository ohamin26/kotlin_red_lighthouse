package kr.ac.kpu.red_lighthouse.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.red_lighthouse.databinding.ActivityFindPasswdBinding
class FindPwActivity : Activity(){

    private  lateinit var binding: ActivityFindPasswdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 아이디 버튼 이벤트
        // 아이디 형식과 다르거나 데이터에 없는 아이디일 경우 경우 다시 입력하게 한다
        // 맞을 경우 새로운 비밀번호 생성 화면 표시
        binding.btnSend.setOnClickListener {
            Firebase.auth.sendPasswordResetEmail(binding.editEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        finish()
                        Log.d("Firebase", "Email sent.")
                        Toast.makeText(applicationContext, "이메일 전송 성공. 받은 메일 안에 링크를 통해 비밀번호를 변경하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}