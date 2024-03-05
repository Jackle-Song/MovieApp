package com.mrsworkshop.movieapp.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnectedOrConnecting ?: false
        }
    }
}