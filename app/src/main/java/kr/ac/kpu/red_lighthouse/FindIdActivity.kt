package kr.ac.kpu.red_lighthouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kr.ac.kpu.red_lighthouse.databinding.ActivityFindidBinding

class FindIdActivity : Activity(){

    private  lateinit var binding: ActivityFindidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lateinit var dataPw : String // 임시 데이터 -비밀번호 저장용
        val dataId = "abc@naver.com" // 임시 데이터 -아이디 저장용
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
            if(CheckUserId().checkEmail(inputId.text.toString()) || !(inputId.text.toString().equals(dataId))){
                Toast.makeText(this@FindIdActivity,"올바른 이메일이 아닙니다", Toast.LENGTH_SHORT).show()
                inputId.requestFocus()
            }else{
                binding.idWindow.visibility = View.GONE
                binding.pwWindow.visibility = View.VISIBLE
            }
        }
        // 비밀번호 버튼 이벤트
        // 비밀번호 형식에 맞지 않거나 공백일 시 다시 입력하게 한다.
        // 맞을 경우 토스트 메시지 출력 및 로그인 화면으로 이동
        newPwBtn.setOnClickListener {
            if(CheckUserId().checkPw(newPw.text.toString()) ||"".equals(newPw.text.toString())){
                Toast.makeText(this@FindIdActivity,"비밀번호는 숫자,문자,특수문자 포함 8~15자 이내로 입력하세요",Toast.LENGTH_SHORT).show()
                newPw.requestFocus()
            }else{
                dataPw = newPw.text.toString()
                Toast.makeText(this@FindIdActivity,"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}