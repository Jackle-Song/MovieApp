package com.mrsworkshop.movieapp.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat


open class BaseActivity : AppCompatActivity() {

    fun setStatusBarLightTheme(option : Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        windowInsetsController.isAppearanceLightStatusBars = option
    }
}