package com.mrsworkshop.movieapp.activity

import android.os.Bundle
import com.mrsworkshop.movieapp.R
import com.mrsworkshop.movieapp.adapter.AuthenticationAdapter
import com.mrsworkshop.movieapp.core.CoreEnum
import com.mrsworkshop.movieapp.databinding.ActivityAuthenticationOptionsBinding

class AuthenticationOptionsActivity : BaseActivity() {
    private lateinit var binding : ActivityAuthenticationOptionsBinding
    private lateinit var authenticationAdapter: AuthenticationAdapter

    private var authOption : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authOption = intent.getIntExtra(AuthenticationActivity.AUTHENTICATION_OPTIONS_VALUE, CoreEnum.AuthenticationOptions.LOGIN.option)

        initUI()
        setupComponentListener()
    }

    /**
     * private function
     */

    private fun initUI() {
        authenticationAdapter = AuthenticationAdapter(this@AuthenticationOptionsActivity)
        binding.viewPagerAuthenticationContent.adapter = authenticationAdapter
        binding.viewPagerAuthenticationContent.isUserInputEnabled = false

        binding.viewPagerAuthenticationContent.setCurrentItem(authOption ?: 0, true)
        when (binding.viewPagerAuthenticationContent.currentItem) {
            CoreEnum.AuthenticationOptions.LOGIN.option -> {
                binding.btnAuthenticationLogin.text = getString(R.string.auth_activity_login_text)
                binding.txtAccountExistedQuestion.text = getString(R.string.auth_activity_do_not_have_account_text)
                binding.txtLoginSignUp.text = getString(R.string.auth_activity_signup_text)
            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                binding.btnAuthenticationLogin.text = getString(R.string.auth_activity_signup_text)
                binding.txtAccountExistedQuestion.text = getString(R.string.auth_activity_already_have_account_text)
                binding.txtLoginSignUp.text = getString(R.string.auth_activity_login_text)
            }
        }
    }

    private fun setupComponentListener() {
        binding.imgBackIcon.setOnClickListener {
            finish()
        }

        binding.txtLoginSignUp.setOnClickListener {
            checkViewPagerCurrentItem()
        }
    }

    private fun checkViewPagerCurrentItem() {
        when (binding.viewPagerAuthenticationContent.currentItem) {
            CoreEnum.AuthenticationOptions.LOGIN.option -> {
                binding.viewPagerAuthenticationContent.setCurrentItem(CoreEnum.AuthenticationOptions.SIGN_UP.option, true)
                binding.btnAuthenticationLogin.text = getString(R.string.auth_activity_signup_text)
                binding.txtAccountExistedQuestion.text = getString(R.string.auth_activity_already_have_account_text)
                binding.txtLoginSignUp.text = getString(R.string.auth_activity_login_text)
            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                binding.viewPagerAuthenticationContent.setCurrentItem(CoreEnum.AuthenticationOptions.LOGIN.option, true)
                binding.btnAuthenticationLogin.text = getString(R.string.auth_activity_login_text)
                binding.txtAccountExistedQuestion.text = getString(R.string.auth_activity_do_not_have_account_text)
                binding.txtLoginSignUp.text = getString(R.string.auth_activity_signup_text)
            }
        }
    }
}