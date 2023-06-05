package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.RegisterActivity
import kr.ac.kpu.red_lighthouse.function.CheckUserId
import kr.ac.kpu.red_lighthouse.databinding.ActivityLoginBinding
import kr.ac.kpu.red_lighthouse.user.User
import kr.ac.kpu.red_lighthouse.user.UserDao
import java.time.LocalDate

class LoginActivity : Activity() {
    private lateinit var binding: ActivityLoginBinding

    var userDao = UserDao()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("user", 0)
        val editor = sharedPreference.edit()

        // 자동 로그인 이벤트 - 자동 로그인 체크한 유저는 유저 정보가 있을 시 메뉴 화면으로 이동한다.
        // 로그아웃 버튼 UserInfoActivity 우측 상단에 있습니다!
        if(sharedPreference.getBoolean("autoLogin", false)){
            if(sharedPreference != null){
                val intent = Intent(applicationContext, MenuSelectActivity::class.java)
                startActivity(intent)
            }
        }

        // 로그인 시도 이벤트
        // 아이디나 비밀번호나 공백이거나 형식에 맞지 않을 시 다시 입력하게 한다.
        // 맞을 경우 페이지 이동 로그인 정보 전송
        binding.btnLogin.setOnClickListener {
            var dialog = LoadingDialog(this)
            dialog.show()
            var email: String = binding.editEmail.text.toString()
            var password: String = binding.ediPassword.text.toString()
            if ("".equals(email)) {
                Toast.makeText(this@LoginActivity, "아이디를 입력하세요", Toast.LENGTH_SHORT).show()
                binding.editEmail.requestFocus()
                dialog.dismiss()
            } else if (!CheckUserId().checkEmail(email)) {
                Toast.makeText(this@LoginActivity, "올바른 이메일이 아닙니다", Toast.LENGTH_SHORT).show()
                binding.editEmail.requestFocus()
                dialog.dismiss()
            } else if ("".equals(password)) {
                Toast.makeText(this@LoginActivity, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                binding.ediPassword.requestFocus()
                dialog.dismiss()
            } else if (!CheckUserId().checkPw(password)) {
                Toast.makeText(
                    this@LoginActivity,
                    "비밀번호는 숫자,문자,특수문자 포함 8~15자 이내로 입력하세요",
                    Toast.LENGTH_SHORT
                ).show()
                binding.ediPassword.requestFocus()
                dialog.dismiss()
            } else if (email == "" && password == "") {
                Toast.makeText(applicationContext, "이메일 혹은 비밀번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT)
                    .show()
                binding.ediPassword.requestFocus()
                dialog.dismiss()
            } else {
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (email != "" && password != "") {
                            userDao.login(email, password).addOnSuccessListener { task ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    val uid: String? = task.user?.uid
                                    if (uid != null) {
                                        var user: User? = User(uid, email, "", "")
                                        if (user != null) {
                                            user = userDao.getDataFromFirebase(user.userId)
                                            if (user != null) {
                                                Log.i("데이터베이스", user.userId)
                                                Log.i("데이터베이스", user.userEmail)
                                                Log.i("데이터베이스", user.userNickname)
                                                Log.i("데이터베이스", user.userDateOfRegist)
                                                editor.putString("userId", user.userId)
                                                editor.putString("userEmail", user.userEmail)
                                                editor.putString("userNickname", user.userNickname)
                                                editor.putString(
                                                    "userDateOfRegist",
                                                    user.userDateOfRegist
                                                )
                                                if(binding.autoLogin.isChecked){
                                                    editor.putBoolean("autoLogin",true)
                                                }else{
                                                    editor.putBoolean("autoLogin",false)
                                                }
                                                editor.apply()
                                                var intent = Intent(
                                                    applicationContext,
                                                    MenuSelectActivity::class.java
                                                )
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "이메일 혹은 비밀번호를 제대로 입력해주세요.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Error) {
                    //에러 메세지 출력
                    Log.e("Firebase", e.toString())
                    Toast.makeText(
                        applicationContext,
                        "로그인에 실패. 이메일 혹은 비밀번호를 제대로 입력해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        // 비밀번호 찾기 화면 이동 이벤트
        binding.btnFindPw.setOnClickListener {
            var intent = Intent(applicationContext, FindPasswordActivity::class.java)
            startActivityForResult(intent, 0)
        }

        binding.btnSign.setOnClickListener {
            var intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivityForResult(intent, 0)
        }


    }
}
