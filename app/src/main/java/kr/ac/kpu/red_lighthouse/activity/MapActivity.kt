package kr.ac.kpu.red_lighthouse.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.databinding.ActivityMapBinding

class MapActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ActivityMapBinding.inflate(inflater, container,false)


        var text = binding.text
        text.text = "Map Activity"
        return binding.root
    }
}