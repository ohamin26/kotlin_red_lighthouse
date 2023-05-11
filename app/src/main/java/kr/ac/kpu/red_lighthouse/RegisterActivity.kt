package kr.ac.kpu.red_lighthouse


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.red_lighthouse.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegisterBinding
    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    private var auth : FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    lateinit var userInfo : HashMap<String,String?>
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

            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            val pw_re = binding.editPwRe.text.toString()
            val name = binding.editName.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(id.isEmpty() || pw.isEmpty() || pw_re.isEmpty() || name.isEmpty()){
                isExistBlank = true
            }
            else{
                if(pw == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame){

                // 회원가입 성공 토스트 메세지 띄우기
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("name", name)
                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()

                // 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }
            else{

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
    private suspend fun createAccount() {
        if (userInfo["email"] != "" && userInfo["password"] != "" && userInfo["nickname"] != "") { //입련한 정보가 null이 아닐 때 실행.
            auth?.createUserWithEmailAndPassword(userInfo["email"].toString(), userInfo["password"].toString()) //계정 생성
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uid : String? = auth.currentUser?.uid
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