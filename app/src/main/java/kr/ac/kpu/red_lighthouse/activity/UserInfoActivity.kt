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
import kr.ac.kpu.red_lighthouse.databinding.ActivityMenuSelectBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityUserInfoBinding
import kr.ac.kpu.red_lighthouse.user.User
import kr.ac.kpu.red_lighthouse.user.UserDao


class UserInfoActivity : Fragment() {
    @SuppressLint("CommitTransaction")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ActivityUserInfoBinding.inflate(inflater, container,false)
        val btnReview = binding.btnReview // 나의 리뷰 페이지 이동
        val btnEdit = binding.btnEdit // 나의 리뷰 페이지 이동
        val btnCs = binding.btnCs         // 고객센터 버튼, 대표 이메일 안내 화면 이동
        val btnInfo = binding.btnInfo     // 앱 정보 화면 이동 버튼
        val btnPolicy = binding.btnPolicy // 개인정보 처리화면 이동 버튼
        val userEmail = binding.userEmail // 사용자 이메일 표시 텍스트 뷰
        val userName = binding.userName   // 사용자 닉네임 표시 텍스트 뷰
        // LoginActivity.kt에서 지정한 preferences(user)값 가져오기
        val prefs = requireContext().getSharedPreferences("user", 0)
        val userId = prefs.getString("userId"," ").toString()
        val userDao = UserDao()
        val edit = prefs.edit()
        //데이터베이스에서 user_nickname,user_email값 가져오기 및 출력
        CoroutineScope(Dispatchers.Main).launch {
            var user: User? = User(userId, "", "", "")
            if (user != null) {
                user = userDao.getDataFromFirebase(user.userId)
                userEmail.text = user?.userEmail
                userName.text = user?.userNickname
            }
        }

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
            startActivity(intent)
        }
        // 고객센터 안내 페이지 이동 이벤트
        btnCs.setOnClickListener {
            val intent = Intent(getActivity(), CsActivity::class.java)
            startActivity(intent)
        }
        // 앱 정보 안내 페이지 이동 이벤트
        btnInfo.setOnClickListener {
            val intent = Intent(getActivity(), AppInfoActivity::class.java)
            startActivity(intent)
        }
        // 개인정보 처리 페이지 이동 이벤트
        btnPolicy.setOnClickListener {
            val intent = Intent(getActivity(), CsActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}

