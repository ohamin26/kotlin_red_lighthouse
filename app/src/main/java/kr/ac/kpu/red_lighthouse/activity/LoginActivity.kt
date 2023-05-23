package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
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

class LoginActivity : Activity(){
    private  lateinit var binding: ActivityLoginBinding


    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // 로그인 시도 이벤트
        // 아이디나 비밀번호나 공백이거나 형식에 맞지 않을 시 다시 입력하게 한다.
        // 맞을 경우 페이지 이동 로그인 정보 전송
        binding.btnLogin.setOnClickListener {
            var dialog = LoadingDialog(this)
            dialog.show()
            var email : String = binding.editEmail.text.toString()
            var password : String = binding.ediPassword.text.toString()
            if("".equals(email)){
                Toast.makeText(this@LoginActivity,"아이디를 입력하세요",Toast.LENGTH_SHORT).show()
                binding.editEmail.requestFocus()
                dialog.dismiss()
            }
            else if(!CheckUserId().checkEmail(email)){
                Toast.makeText(this@LoginActivity,"올바른 이메일이 아닙니다",Toast.LENGTH_SHORT).show()
                binding.editEmail.requestFocus()
                dialog.dismiss()
            }
            else if("".equals(password)){
                Toast.makeText(this@LoginActivity,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show()
                binding.ediPassword.requestFocus()
                dialog.dismiss()
            }
            else if(!CheckUserId().checkPw(password)){
                Toast.makeText(this@LoginActivity,"비밀번호는 숫자,문자,특수문자 포함 8~15자 이내로 입력하세요",Toast.LENGTH_SHORT).show()
                binding.ediPassword.requestFocus()
                dialog.dismiss()
            }
            else{
                try {
                    Log.i("Firebase", "로그인 : $email , $password")
                    CoroutineScope(Dispatchers.Main).launch {
                        val ref = db.collection("users")
                        if (email != "" && password !="") {
                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    val user = auth.currentUser
                                    if (user != null) {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            val userInfo: User? =  ref.document(user.uid).get().await().toObject(User::class.java)
                                            val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
                                            val editor = sharedPreference.edit()
                                            if (userInfo != null) {
                                                editor.putString("user_id",userInfo.user_id)
                                                editor.putString("user_email",userInfo.user_email)
                                                editor.putString("user_nickname",userInfo.user_nickname)
                                                editor.putString("user_dateOfRegist",userInfo.user_dateOfRegist)
                                                editor.apply()
                                                Toast.makeText(applicationContext, "로그인 성공. ${userInfo.user_nickname} 님 환영합니다.", Toast.LENGTH_SHORT).show()
                                                var intent = Intent(applicationContext, MenuSelectActivity::class.java)
                                                startActivity(intent)
                                            }else{
                                                Toast.makeText(applicationContext, "이메일 혹은 비밀번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(applicationContext, "로그인에 실패. 이메일 혹은 비밀번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }else{
                            Toast.makeText(applicationContext, "이메일 혹은 비밀번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e : Error){
                    //에러 메세지 출력
                    Log.e("Firebase", e.toString())
                    Toast.makeText(applicationContext, "로그인에 실패. 이메일 혹은 비밀번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // 비밀번호 찾기 화면 이동 이벤트
        binding.forgetpasswd.setOnClickListener {
            var intent = Intent(applicationContext, FindPasswordActivity::class.java)
            startActivityForResult(intent,0)
        }

        binding.btnSign.setOnClickListener {
            var intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivityForResult(intent,0)
        }

    }
}
