package com.mrsworkshop.movieapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mrsworkshop.movieapp.core.CoreEnum
import com.mrsworkshop.movieapp.databinding.ActivityAuthenticationBinding
import com.mrsworkshop.movieapp.utils.Constants

class AuthenticationActivity : BaseActivity() {

    companion object {
        const val AUTHENTICATION_OPTIONS_VALUE = "authenticationOptionsValue"
    }

    private lateinit var binding : ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupComponentListener()
    }

    /**
     * private function
     */

    private fun setupComponentListener() {
        binding.btnRedirectLogin.setOnClickListener {
            switchToAuthenticationOptions(CoreEnum.AuthenticationOptions.LOGIN.option)
        }

        binding.btnRedirectSignup.setOnClickListener {
            switchToAuthenticationOptions(CoreEnum.AuthenticationOptions.SIGN_UP.option)
        }
    }

    private fun switchToAuthenticationOptions(option : Int) {
        val intent = Intent(this@AuthenticationActivity, AuthenticationOptionsActivity::class.java)
        intent.putExtra(AUTHENTICATION_OPTIONS_VALUE, option)
        startActivity(intent)
    }
}