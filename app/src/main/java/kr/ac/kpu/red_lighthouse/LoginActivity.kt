package kr.ac.kpu.red_lighthouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kr.ac.kpu.red_lighthouse.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : Activity(){

    private  lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId : EditText = binding.userId
        val userPw : EditText = binding.userPw
        val userPwFindBtn : Button = binding.userPwFindBtn
        val loginSubmitBtn : Button = binding.loginSubmitBtn
        // 로그인 시도 이벤트
        // 아이디나 비밀번호나 공백이거나 형식에 맞지 않을 시 다시 입력하게 한다.
        // 맞을 경우 페이지 이동 로그인 정보 전송
        loginSubmitBtn.setOnClickListener {
            if("".equals(userId.text.toString())){
                Toast.makeText(this@LoginActivity,"아이디를 입력하세요",Toast.LENGTH_SHORT).show()
                userId.requestFocus()
            }
            else if(!CheckUserId().checkEmail(userId.text.toString())){
                Toast.makeText(this@LoginActivity,"올바른 이메일이 아닙니다",Toast.LENGTH_SHORT).show()
                userId.requestFocus()
            }
            else if("".equals(userPw.text.toString())){
                Toast.makeText(this@LoginActivity,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show()
                userPw.requestFocus()
            }
            else if(!CheckUserId().checkPw(userPw.text.toString())){
                Toast.makeText(this@LoginActivity,"비밀번호는 숫자,문자,특수문자 포함 8~15자 이내로 입력하세요",Toast.LENGTH_SHORT).show()
                userPw.requestFocus()
            }
            else{
                var intent = Intent(applicationContext, MapActivity::class.java)
                intent.putExtra("id",userId.text.toString())
                intent.putExtra("pw",userPw.text.toString())
                startActivity(intent)
            }
        }
        // 비밀번호 찾기 화면 이동 이벤트
        userPwFindBtn.setOnClickListener {
            var intent = Intent(applicationContext, FindIdActivity::class.java)
            startActivityForResult(intent,0)
        }

    }
}
