package kr.ac.kpu.red_lighthouse

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.databinding.ActivityRegisterBinding
import kr.ac.kpu.red_lighthouse.user.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegisterBinding
    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth


        val btn_register: Button = binding.btnRegister
        val edit_pw: EditText = binding.editPw
        val edit_id: EditText = binding.editId
        val edit_pw_re: EditText = binding.editPwRe
        val edit_name: EditText = binding.editName

        btn_register.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val email = binding.editId.text.toString()
            val password = binding.editPw.text.toString()
            val pw_re = binding.editPwRe.text.toString()
            val nickname = binding.editName.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if (email.isEmpty() || password.isEmpty() || pw_re.isEmpty() || nickname.isEmpty()) {
                isExistBlank = true
            } else {

                if (password == pw_re) {
                    isPWSame = true
                }
            }

            if (!isExistBlank && isPWSame) {
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        val ref = db.collection("users")
                        val onlyDate: LocalDate = LocalDate.now()
                        var checkUser : List<User> = ref.whereEqualTo("user_email",email).get().await().toObjects(User::class.java)
                        if(checkUser.isNotEmpty()) {//데이터베이스 안에 같은 이메일이 존재 한다면 알려주는 메세지.
                            Toast.makeText(applicationContext, "이미 가입된 계정입니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            var user  = User("",email,password,nickname,onlyDate.toString())
                            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener()
                            { task ->
                                if (task.isSuccessful) {
                                    val uid : String? = auth.currentUser?.uid
                                    if(uid !=null){ //UserId가 null이 아닐 때 데이터베이스 정보 저장 명령 실행
                                        user.user_id = uid;
                                        ref.document(uid).set(user)
                                        finish() // 가입창 종료
                                        Toast.makeText(applicationContext, "회원가입 성공. 로그인해주세요.", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    //회원가입 실패 메세지 출력
                                    Toast.makeText(applicationContext, "회원가입에 실패했습니다. 나중에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                }catch (e : Error){
                    //에러 메세지 출력
                    print(e)
                    Toast.makeText(applicationContext, "회원가입에 실패했습니다. 나중에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 상태에 따라 다른 다이얼로그 띄워주기
                if (isExistBlank) {   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                } else if (!isPWSame) { // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                }
            }

        }
    }
    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        if(!CheckUserId().checkEmail(binding.editId.text.toString())){
            Toast.makeText(this@RegisterActivity,"올바른 이메일이 아닙니다",Toast.LENGTH_SHORT).show()
            binding.editId.requestFocus()
        }

        if(!CheckUserId().checkPw(binding.editPw.text.toString())){
            Toast.makeText(this@RegisterActivity,"올바른 비밀번호 형식이 아닙니다",Toast.LENGTH_SHORT).show()
            binding.editPw.requestFocus()
        }
        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        // 입력한 비밀번호가 다를 경우
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}
