package kr.ac.kpu.red_lighthouse.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.ac.kpu.red_lighthouse.R

class ChangeInfoActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_info)
        // 이메일이랑 닉네임 따로따로 변경해야하니까
        // 따로 만들어야할거 같아
        // 예를들어 어떤 걸 수정할건지 선택지를 주고 한번 더 들어가서 수정을 한다든가.
        // 이메일. 닉네임. 비밀번호 변경 수정해야함
    }
}