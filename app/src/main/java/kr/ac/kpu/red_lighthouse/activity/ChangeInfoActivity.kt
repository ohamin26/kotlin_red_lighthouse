package kr.ac.kpu.red_lighthouse.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityChangeInfoBinding
import kr.ac.kpu.red_lighthouse.user.User
import kr.ac.kpu.red_lighthouse.user.UserDao

class ChangeInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = binding.userEmail // 사용자 이메일 표시 텍스트 뷰
        val userName = binding.userName   // 사용자 닉네임 표시 텍스트 뷰
        val btnEditNickname = binding.btnEditNickname
        val btnEditPw = binding.btnEditPw
        val prefs = getSharedPreferences("user", 0)

        //닉네임 변경 이벤트가 적용 안될 시 아래 코드 두 줄 주석처리하고 사용하기!
        /*val userId = prefs.getString("userId"," ").toString()
        val userDao = UserDao()
        //데이터베이스에서 user_nickname,user_email값 가져오기 및 출력
        CoroutineScope(Dispatchers.Main).launch {
            var user: User? = User(userId, "", "", "")
            if (user != null) {
                user = userDao.getDataFromFirebase(user.userId)
                userEmail.text = user?.userEmail
                userName.text = user?.userNickname
            }
        }*/

        //이메일, 닉네임 표시
        //닉네임 변경 시 변경된 닉네임 적용 안될 시 위에 코드 사용!
        userEmail.text = prefs.getString("userEmail","").toString()
        userName.text = prefs.getString("userNickname","").toString()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MenuSelectActivity::class.java) // 다른 액티비티의 이름으로 변경해야 합니다.
            intent.putExtra("userNicknameChange",true)
            startActivity(intent)
            finish()
        }

        // 닉네임 변경 이벤트
        btnEditNickname.setOnClickListener {
            var intent = Intent(applicationContext, ChangeInfoNicknameActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 비밀번호 변경 이벤트
        btnEditPw.setOnClickListener {
            var intent = Intent(applicationContext, FindPasswordActivity::class.java)
            startActivity(intent) }
    }
}