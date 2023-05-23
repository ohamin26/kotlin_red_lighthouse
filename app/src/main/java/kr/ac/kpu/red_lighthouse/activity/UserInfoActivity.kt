package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityLoginBinding
import kr.ac.kpu.red_lighthouse.databinding.ActivityUserInfoBinding


class UserInfoActivity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ActivityUserInfoBinding.inflate(inflater, container,false)
        val btnEdit = binding.btnEdit     // 이메일, 닉네임 정보 수정 페이지 이동 버튼
        val btnReview = binding.btnReview // 나의 리뷰 페이지 이동
        val btnCs = binding.btnCs         // 고객센터 버튼, 대표 이메일 안내 화면 이동
        val btnInfo = binding.btnInfo     // 앱 정보 화면 이동 버튼
        val btnPolicy = binding.btnPolicy // 개인정보 처리화면 이동 버튼

        // 정보 수정 페이지 이동 이벤트
        btnEdit.setOnClickListener {
            val intent = Intent(getActivity(), ChangeInfoActivity::class.java)
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