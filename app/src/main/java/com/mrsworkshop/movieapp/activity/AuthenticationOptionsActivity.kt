package com.mrsworkshop.movieapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mrsworkshop.movieapp.R
import com.mrsworkshop.movieapp.adapter.AuthenticationAdapter
import com.mrsworkshop.movieapp.core.CoreEnum
import com.mrsworkshop.movieapp.databinding.ActivityAuthenticationOptionsBinding


class AuthenticationOptionsActivity : BaseActivity(), AuthenticationAdapter.AuthenticationOptionsInterface {
    private lateinit var binding : ActivityAuthenticationOptionsBinding
    private lateinit var authenticationAdapter: AuthenticationAdapter
    private lateinit var mAuth : FirebaseAuth

    private var authOption : Int? = null

    private var isSignUpValid : Boolean = false
    private var signUpEmail : String? = null
    private var signUpPassword : String? = null

    private var isLoginValid : Boolean = false
    private var loginEmail : String? = null
    private var loginPassword : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        authOption = intent.getIntExtra(AuthenticationActivity.AUTHENTICATION_OPTIONS_VALUE, CoreEnum.AuthenticationOptions.LOGIN.option)

        checkSignUpButtonValidation(false)
        initUI()
        setupComponentListener()
    }

    override fun insertEmailAddress(email: String?, isValid: Boolean) {
        when (binding.viewPagerAuthenticationContent.currentItem) {
            CoreEnum.AuthenticationOptions.LOGIN.option -> {
                loginEmail = email
                checkLoginButtonValidation(isValid)
            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                signUpEmail = email
                checkSignUpButtonValidation(isValid)
            }
        }
    }

    override fun insertPassword(password: String?, isValid: Boolean) {
        when (binding.viewPagerAuthenticationContent.currentItem) {
            CoreEnum.AuthenticationOptions.LOGIN.option -> {
                loginPassword = password
                checkLoginButtonValidation(isValid)
            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                signUpPassword = password
                checkSignUpButtonValidation(isValid)
            }
        }
    }

    /**
     * private function
     */

    private fun initUI() {
        authenticationAdapter = AuthenticationAdapter(this@AuthenticationOptionsActivity, this@AuthenticationOptionsActivity)
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

        binding.btnAuthenticationLogin.setOnClickListener {
            when (binding.viewPagerAuthenticationContent.currentItem) {
                CoreEnum.AuthenticationOptions.LOGIN.option -> {
                    loginUser(loginEmail ?: "", loginPassword ?: "")
                }

                CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                    registerUser(signUpEmail ?: "", signUpPassword ?: "")
                }
            }
        }

        binding.txtLoginSignUp.setOnClickListener {
            when (binding.viewPagerAuthenticationContent.currentItem) {
                CoreEnum.AuthenticationOptions.LOGIN.option -> {
                    checkSignUpButtonValidation(isSignUpValid)
                }

                CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                    checkLoginButtonValidation(isLoginValid)
                }
            }
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

    private fun registerUser(email : String, password : String) {
        showLoadingViewDialog()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@AuthenticationOptionsActivity, getString(R.string.auth_activity_sign_up_successful_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()

                    val intent = Intent(this@AuthenticationOptionsActivity, MovieListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@AuthenticationOptionsActivity, getString(R.string.auth_activity_sign_up_failed_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()
                }
            }
    }

    private fun loginUser(email : String, password : String) {
        showLoadingViewDialog()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@AuthenticationOptionsActivity, getString(R.string.auth_activity_login_successful_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()

                    val intent = Intent(this@AuthenticationOptionsActivity, MovieListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@AuthenticationOptionsActivity, getString(R.string.auth_activity_login_failed_text), Toast.LENGTH_LONG).show()
                    dismissLoadingViewDialog()
                }
            }
    }

    private fun checkSignUpButtonValidation(isValid: Boolean) {
        if (!isValid || signUpEmail.isNullOrEmpty() || signUpPassword.isNullOrEmpty()) {
            binding.btnAuthenticationLogin.alpha = 0.5f
            binding.btnAuthenticationLogin.isEnabled = false
            isSignUpValid = false
        }
        else {
            binding.btnAuthenticationLogin.alpha = 1f
            binding.btnAuthenticationLogin.isEnabled = true
            isSignUpValid = true
        }
    }

    private fun checkLoginButtonValidation(isValid: Boolean) {
        if (!isValid || loginEmail.isNullOrEmpty() || loginPassword.isNullOrEmpty()) {
            binding.btnAuthenticationLogin.alpha = 0.5f
            binding.btnAuthenticationLogin.isEnabled = false
            isLoginValid = false
        }
        else {
            binding.btnAuthenticationLogin.alpha = 1f
            binding.btnAuthenticationLogin.isEnabled = true
            isLoginValid = true
        }
    }
}