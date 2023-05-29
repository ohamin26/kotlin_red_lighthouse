package kr.ac.kpu.red_lighthouse

import android.content.Context
import android.widget.Toast

class EventMessage {
    fun registerErr(applicationContext: Context){
        Toast.makeText(
            applicationContext,
            "회원가입에 실패했습니다. 나중에 다시 시도해주세요.",
            Toast.LENGTH_SHORT
        ).show()
    }
    fun emailExists(applicationContext: Context){
        Toast.makeText(applicationContext, "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT)
            .show()
    }
    fun nicknameExists(applicationContext: Context){
        Toast.makeText(applicationContext, "닉네임이 이미 존재합니다.", Toast.LENGTH_SHORT)
            .show()
    }
}