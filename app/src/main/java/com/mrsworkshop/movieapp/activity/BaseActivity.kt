package com.mrsworkshop.movieapp.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.mrsworkshop.movieapp.component.LoadingDialog


open class BaseActivity : AppCompatActivity() {

    private lateinit var loadingViewDialog: LoadingDialog

    fun setStatusBarLightTheme(option : Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        windowInsetsController.isAppearanceLightStatusBars = option
    }

    fun showLoadingViewDialog() {
        loadingViewDialog = LoadingDialog.show(supportFragmentManager)
    }

    fun dismissLoadingViewDialog() {
        loadingViewDialog.dismiss()
    }
}