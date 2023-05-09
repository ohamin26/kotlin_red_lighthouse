package kr.ac.kpu.red_lighthouse

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kr.ac.kpu.red_lighthouse.databinding.ActivityFindidBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class FindIdActivity : Activity(){

    private  lateinit var binding: ActivityFindidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lateinit var pw : String
        val dataId = "abc@naver.com"
        val inputId = binding.findId
        val checkIdBtn = binding.checkIdBtn
        val newPwBtn = binding.newPwBtn
        var findPw = binding.findPw
        fun checkEmail(): Boolean {
            val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val check = inputId.text.toString().trim()
            return Pattern.matches(emailValidation, check)
        }
        checkIdBtn.setOnClickListener {
            if(!checkEmail() || dataId.equals(inputId.text.toString())){
                Toast.makeText(this@FindIdActivity,"올바른 이메일이 아닙니다", Toast.LENGTH_SHORT).show()
                inputId.requestFocus()
            }else{
                binding.idWindow.visibility = View.GONE
                binding.pwWindow.visibility = View.VISIBLE
            }
        }
        newPwBtn.setOnClickListener {
            pw = findPw.text.toString()
            Toast.makeText(this@FindIdActivity,"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show()
            newPwBtn.visibility = View.GONE
        }
    }
}