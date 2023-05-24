package kr.ac.kpu.red_lighthouse

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.activity.LoadingDialog
import kr.ac.kpu.red_lighthouse.databinding.ActivityRegisterBinding
import kr.ac.kpu.red_lighthouse.function.CheckUserId
import kr.ac.kpu.red_lighthouse.user.User
import java.time.LocalDate


class RegisterActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegisterBinding
    private val TAG: String = "Register"
    private var isExistBlank = false
    private var isPWSame = false
    private var isEmailtrue = false
    private var isPWtrue = false

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

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
                    dialog.show() //로딩바 실행
                    CoroutineScope(Dispatchers.Main).launch {
                        val ref = db.collection("users")
                        val onlyDate: LocalDate = LocalDate.now()
                        var checkUser: List<User> =
                            ref.whereEqualTo("user_email", email).get().await()
                                .toObjects(User::class.java)
                        if (checkUser.isNotEmpty()) {
                            //데이터베이스 안에 같은 이메일이 존재 한다면 알려주는 메세지.
                            Toast.makeText(applicationContext, "이미 가입된 계정입니다.", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            var user = User("", email, nickname, onlyDate.toString())
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener()
                                { task ->
                                    if (task.isSuccessful) {
                                        val uid: String? = auth.currentUser?.uid
                                        if (uid != null) {
                                            //UserId가 null이 아닐 때 데이터베이스 정보 저장 명령 실행
                                            user.user_id = uid;
                                            ref.document(uid).set(user)
                                            Toast.makeText(
                                                applicationContext,
                                                "회원가입 성공. 로그인해주세요.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
                                        }
                                    } else {
                                        //회원가입 실패 메세지 출력
                                        Toast.makeText(
                                            applicationContext,
                                            "회원가입에 실패했습니다. 나중에 다시 시도해주세요.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }

                    }
                } catch (e: Error) {
                    //에러 메세지 출력
                    print(e)
                    Toast.makeText(
                        applicationContext,
                        "회원가입에 실패했습니다. 나중에 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }
            }
        }
    }
}
