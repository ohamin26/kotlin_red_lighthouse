package kr.ac.kpu.red_lighthouse.function

import java.util.regex.Pattern

class CheckUserId {
    //아이디 형식 검사
    fun checkEmail(email : String): Boolean {
        val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val check = email.trim()
        return Pattern.matches(emailValidation, check)
    }
    //비밀번호 형식 검사
    fun checkPw(pw : String): Boolean {
        val pwValidation = "^.*(?=^.{6,15}\$)(?=.*\\d)(?=.*[a-zA-Z]).*\$"
        val check = pw.trim()
        return Pattern.matches(pwValidation, check)
    }
}