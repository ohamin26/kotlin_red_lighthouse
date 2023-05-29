package kr.ac.kpu.red_lighthouse

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.metrics.Event
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.core.EventManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.activity.LoadingDialog
import kr.ac.kpu.red_lighthouse.activity.LoginActivity
import kr.ac.kpu.red_lighthouse.activity.MenuSelectActivity
import kr.ac.kpu.red_lighthouse.databinding.ActivityRegisterBinding
import kr.ac.kpu.red_lighthouse.function.CheckUserId
import kr.ac.kpu.red_lighthouse.user.User
import kr.ac.kpu.red_lighthouse.user.UserDao
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegisterBinding
    private val TAG: String = "Register"
    private var isExistBlank = false
    private var isPWSame = false
    private var isEmailtrue = false
    private var isPWtrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val eventMessage = EventMessage()
        val userDao = UserDao()

        var dialog = LoadingDialog(this) //로딩바 선언

        val btn_register: Button = binding.btnRegister

        binding.btnBack.setOnClickListener {
            finish()
        }
        btn_register.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")
            val email = binding.editEmail.text.toString()
            val password = binding.editPw.text.toString()
            val pwCheck = binding.editPwRe.text.toString()
            val nickname =  binding.editNickname.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if (email.isEmpty() || password.isEmpty() || pwCheck.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "회원가입에 실패하였습니다. 작성하지 않은 부분이 있습니다",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (password == pwCheck) {
                    isPWSame = true
                }
            }

            if (!CheckUserId().checkEmail(binding.editEmail.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "회원가입에 실패했습니다. 이메일 형식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.editEmail.requestFocus()
            }else{
                isEmailtrue = true
            }

            if (!CheckUserId().checkPw(binding.editPw.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "회원가입에 실패했습니다. 비밀번호 형식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.editEmail.requestFocus()
            }else{
                isPWtrue = true
            }

            // 입력한 비밀번호가 다를 경우
            if (password != pwCheck) {
                Toast.makeText(
                    applicationContext,
                    "회원가입에 실패했습니다. 비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (isPWSame && isEmailtrue && isPWtrue) {
                try {
                    dialog.show() //로딩바 실행.
                    CoroutineScope(Dispatchers.Main).launch {
                        val onlyDate: LocalDate = LocalDate.now()
                        var user = User("", email, nickname, onlyDate.toString())
                        var checkEmail: List<User> = userDao.checkExistsWithEmail(email)
                        var checkNickname: List<User> = userDao.checkExistsWithNickname(nickname)
                        if (checkEmail.isNotEmpty()) {
                            //데이터베이스 안에 같은 이메일이 존재 한다면 알려주는 메세지.
                            eventMessage.emailExists(applicationContext)
                            dialog.dismiss()
                        } else if(checkNickname.isNotEmpty()){
                            eventMessage.nicknameExists(applicationContext)
                            dialog.dismiss()
                        } else{
                            userDao.register(email,password).addOnSuccessListener {
                                task ->
                                Log.i("데이터베이스", "회원가입 완료.")
                                val uid :String? = task.user?.uid
                                if(uid!=null){
                                    user.userId = uid
                                    userDao.setDataToFirebase(user).addOnSuccessListener {
                                        Log.i("데이터베이스", "데이터 저장 완료.")
                                        Toast.makeText(
                                            applicationContext,
                                            "회원가입 성공. 로그인해주세요.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog.dismiss()
                                        finish()
                                    }.addOnFailureListener{
                                        eventMessage.registerErr(applicationContext)
                                        dialog.dismiss()
                                    }
                                }
                            }.addOnFailureListener {
                                eventMessage.registerErr(applicationContext)
                                dialog.dismiss()
                            }
                        }

                    }
                } catch (e: Error) {
                    //에러 메세지 출력
                    print(e)
                    eventMessage.registerErr(applicationContext)
                    dialog.dismiss()
                }
            }
        }
    }
}