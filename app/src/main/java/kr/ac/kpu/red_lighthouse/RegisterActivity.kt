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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.ac.kpu.red_lighthouse.databinding.ActivityRegisterBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegisterBinding
    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    private var auth : FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    lateinit var userInfo : HashMap<String,String?>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btn_register : Button = binding.btnRegister
        val edit_pw : EditText = binding.editPw
        val edit_id : EditText = binding.editId
        val edit_pw_re : EditText = binding.editPwRe
        val edit_name: EditText = binding.editName

        btn_register.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val email = binding.editId.text.toString()
            val password = binding.editPw.text.toString()
            val pw_re = binding.editPwRe.text.toString()
            val nickname = binding.editName.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(email.isEmpty() || password.isEmpty() || pw_re.isEmpty() || nickname.isEmpty()){
                isExistBlank = true
            }
            else{

                if(password == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame){
                userInfo = hashMapOf(
                    "uid" to "",
                    "email" to email,
                    "password" to password,
                    "nickname" to nickname,
                    "dateOfRegist" to ""
                )
                CoroutineScope(Dispatchers.Main).launch {
                    createAccount()
                }
         } else{

               // 상태에 따라 다른 다이얼로그 띄워주기
               if(isExistBlank){   // 작성 안한 항목이 있을 경우
                    dialog("blank")
              }
               else if(!isPWSame){ // 입력한 비밀번호가 다를 경우
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
    // 계정 생성
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createAccount() {
        if (userInfo["email"] != "" && userInfo["password"] != "" && userInfo["nickname"] != "") { //입련한 정보가 null이 아닐 때 실행.
            auth?.createUserWithEmailAndPassword(userInfo["email"].toString(), userInfo["password"].toString()) //계정 생성
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val onlyDate: LocalDate = LocalDate.now()
                        val uid : String? = auth.currentUser?.uid
                        userInfo["uid"] = uid
                        userInfo["dateOfRegist"]=onlyDate.toString()
                        if(uid !=null){ //UserId가 null이 아닐 때 데이터베이스 정보 저장 명령 실행
                            db.collection("users").document() //데이터베이스 정보 저장
                                .set(userInfo)
                                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                        }
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}