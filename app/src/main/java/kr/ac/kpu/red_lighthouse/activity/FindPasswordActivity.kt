package kr.ac.kpu.red_lighthouse.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.red_lighthouse.databinding.ActivityFindPasswdBinding

class FindPasswordActivity : Activity(){

    private  lateinit var binding: ActivityFindPasswdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            finish()
        }
        // 아이디 버튼 이벤트
        // 아이디 형식과 다르거나 데이터에 없는 아이디일 경우 경우 다시 입력하게 한다
        // 맞을 경우 새로운 비밀번호 생성 화면 표시
        binding.btnSend.setOnClickListener {
            if(binding.editEmail.text.isNotEmpty()){ //이메일 작성 칸이 비어있지 않으면 실행.
                var dialog = LoadingDialog(this) //로딩바 실행
                dialog.show()
                //데이터베이스에 접근하는 명령어
                Firebase.auth.sendPasswordResetEmail(binding.editEmail.text.toString())
                    .addOnCompleteListener { task -> //결과를 가져온다.
                        if (task.isSuccessful) { //결과가 성공이면 실행되는 if문
                            finish() //로그인 화면으로 돌아가게 함.
                            Log.d("Firebase", "Email sent.")
                            Toast.makeText(applicationContext, "이메일 전송 성공. 받은 메일 안에 링크를 통해 비밀번호를 변경하세요.", Toast.LENGTH_SHORT).show()
                        }else {
                            dialog.dismiss()
                            Toast.makeText(applicationContext, "이메일 전송 실패. 작성한 이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }

                    }
            }


        }
    }
}