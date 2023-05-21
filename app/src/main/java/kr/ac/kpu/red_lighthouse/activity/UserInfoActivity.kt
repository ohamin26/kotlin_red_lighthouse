package kr.ac.kpu.red_lighthouse.activity

import android.annotation.SuppressLint
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

        var text = binding.text
        text.text = "userinfo"
        return binding.root
    }
}