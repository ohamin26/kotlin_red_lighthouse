package kr.ac.kpu.red_lighthouse.activity

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import kr.ac.kpu.red_lighthouse.R

class LoadingDialog constructor(context: Context) : Dialog(context){
    init {
        setCanceledOnTouchOutside(false)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.dialog_loading)
    }
}