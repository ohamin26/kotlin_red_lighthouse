package kr.ac.kpu.red_lighthouse.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import kr.ac.kpu.red_lighthouse.databinding.ActivityLocationAddBinding


class LocationAddActivity : Fragment() {
    private val GALLERY = 1
    private lateinit var binding: ActivityLocationAddBinding
    private var cntImg : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityLocationAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                val imageData: Uri? = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        imageData
                    )
                    // 이미지뷰를 생성합니다.
                    if(cntImg < 3) {
                        val imageView = ImageView(context)
                        var displayMetrics = resources.displayMetrics
                        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        imageView.setImageBitmap(bitmap)
                        val imageLayoutParams = LinearLayout.LayoutParams(
                            Math.round(200 * displayMetrics.density),
                            Math.round(150 * displayMetrics.density)
                        )
                        imageLayoutParams.leftMargin = Math.round(10 * displayMetrics.density)
                        imageView.setLayoutParams(imageLayoutParams)

                        binding.layoutImagelist.addView(imageView)
                        cntImg += 1
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}


