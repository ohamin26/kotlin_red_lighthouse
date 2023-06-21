package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityUserInfoBinding
import kr.ac.kpu.red_lighthouse.user.User
import kr.ac.kpu.red_lighthouse.user.UserDao


class UserInfoActivity : Fragment() {
    @SuppressLint("CommitTransaction", "CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ActivityUserInfoBinding.inflate(inflater, container,false)
        // LoginActivity.kt에서 지정한 preferences(user)값 가져오기
        val prefs = requireContext().getSharedPreferences("user", 0)

        val edit = prefs.edit()
        //닉네임 변경 이벤트가 적용 안될 시 아래 코드 두 줄 주석처리하고 사용하기!
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

        //바텀 바 버튼
        val btn = (activity as MenuSelectActivity).findViewById<BottomNavigationView>(R.id.bnv_main)

        //바텀 바 버튼의 포커스가 안맞을 시 포커스를 맞춰준다.
        if(btn.selectedItemId != R.id.third){
            btn.selectedItemId = R.id.third
        }
        // 로그아웃 이벤트
        binding.btnLogout.setOnClickListener {
            edit.clear()
            edit.apply()
            val intent = Intent(getActivity(), LoginActivity::class.java)
            val sharedPreference = this.getActivity()?.getSharedPreferences("user", 0)
            val editor = sharedPreference?.edit()
            editor?.putBoolean("autoLogin",false)
            editor?.apply()
            startActivity(intent)
        }
        // 정보 수정 페이지 이동 이벤트
        binding.btnEdit.setOnClickListener {
            val intent = Intent(getActivity(), ChangeInfoActivity::class.java)
            startActivity(intent)
        }
        // 고객센터 안내 페이지 이동 이벤트
        binding.btnCs.setOnClickListener {
            val intent = Intent(getActivity(), CsActivity::class.java)
            startActivity(intent)
        }
        // 앱 정보 안내 페이지 이동 이벤트
        binding.btnInfo.setOnClickListener {
            val intent = Intent(getActivity(), AppInfoActivity::class.java)
            startActivity(intent)
        }
        // 개인정보 처리 페이지 이동 이벤트
        binding.btnPolicy.setOnClickListener {
            val intent = Intent(getActivity(), PolicyActivity::class.java)
            startActivity(intent)
        }
        // 나의 리뷰기록 페이지 이동 이벤트
        binding.btnReview.setOnClickListener {
            val intent = Intent(getActivity(), MyReviewActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}

