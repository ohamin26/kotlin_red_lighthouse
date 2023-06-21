package kr.ac.kpu.red_lighthouse.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.ac.kpu.red_lighthouse.databinding.ActivityChangeInfoBinding
class ChangeInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnEditNickname = binding.btnEditNickname
        val btnEditPw = binding.btnEditPw
        val prefs = getSharedPreferences("user", 0)
        //데이터베이스에서 user_nickname,user_email값 가져오기 및 출력
        /*CoroutineScope(Dispatchers.Main).launch {
            val userId = prefs.getString("userId"," ").toString()
            val userDao = UserDao()
            var user: User? = User(userId, "", "", "")
            if (user != null) {
                user = userDao.getDataFromFirebase(user.userId)
                userEmail.text = user?.userEmail
                userName.text = user?.userNickname
            }
        }*/

        //이메일, 닉네임 표시
        //닉네임 변경 시 변경된 닉네임 적용 안될 시 위에 코드 사용!
        binding.userEmail.text = prefs.getString("userEmail","").toString()
        binding.userName.text = prefs.getString("userNickname","").toString()

        binding.btnBack.setOnClickListener {
            var intent = Intent(applicationContext, MenuSelectActivity::class.java)
            intent.putExtra("user_info_changed",true)
            startActivity(intent)
            finish()
        }

        btnEditNickname.setOnClickListener {
            var intent = Intent(applicationContext, ChangeInfoNicknameActivity::class.java)
            startActivity(intent) }

        btnEditPw.setOnClickListener {
            var intent = Intent(applicationContext, FindPasswordActivity::class.java)
            startActivity(intent) }
    }
}