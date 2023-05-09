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
        val id : EditText = binding.loginId
        val pw : EditText = binding.loginPw
        val loginFind : Button = binding.findLogin
        val loginSubmit : Button = binding.login
        fun checkEmail(): Boolean {
            val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val check = id.text.toString().trim()
            return Pattern.matches(emailValidation, check)
        }
        loginSubmit.setOnClickListener {
            if("".equals(id.text.toString())){
                Toast.makeText(this@LoginActivity,"아이디를 입력하세요",Toast.LENGTH_SHORT).show()
                id.requestFocus()
            }
            else if(!checkEmail()){
                Toast.makeText(this@LoginActivity,"올바른 이메일이 아닙니다",Toast.LENGTH_SHORT).show()
                id.requestFocus()
            }
            else if("".equals(pw.text.toString())){
                Toast.makeText(this@LoginActivity,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show()
                pw.requestFocus()
            }
            else{
                var intent = Intent(applicationContext, MapActivity::class.java)
                intent.putExtra("id",id.text.toString())
                intent.putExtra("pw",pw.text.toString())
                startActivity(intent)
            }
        }
        loginFind.setOnClickListener {
            var intent = Intent(applicationContext, FindIdActivity::class.java)
            startActivityForResult(intent,0)
        }

    }
}
