package kr.ac.kpu.red_lighthouse

import android.app.Application

class PreferenceUse: Application() {
    companion object{
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}