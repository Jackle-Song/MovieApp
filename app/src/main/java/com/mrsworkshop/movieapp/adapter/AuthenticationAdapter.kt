package com.mrsworkshop.movieapp.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.mrsworkshop.movieapp.R
import com.mrsworkshop.movieapp.core.CoreEnum


class AuthenticationAdapter(
    private val mContext : Context,
    private val mListener : AuthenticationOptionsInterface,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface AuthenticationOptionsInterface {
        fun insertEmailAddress(email : String?, isValid : Boolean)
        fun insertPassword(password : String?, isValid : Boolean)
    }

    class LoginViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tilOutlinedLoginEmailTextField : TextInputLayout = itemView.findViewById(R.id.tilOutlinedLoginEmailTextField)
        val etEditTextEmailUsername : EditText = itemView.findViewById(R.id.etEditTextEmailUsername)
        val etEditTextPassword : EditText = itemView.findViewById(R.id.etEditTextPassword)
    }

    class SignUpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etEditTextEmail : EditText = itemView.findViewById(R.id.etEditTextEmail)
        val etEditTextPassword : EditText = itemView.findViewById(R.id.etEditTextPassword)
        val tilOutlinedSignUpEmailTextField : TextInputLayout = itemView.findViewById(R.id.tilOutlinedSignUpEmailTextField)
        val tilOutlinedPasswordTextField : TextInputLayout = itemView.findViewById(R.id.tilOutlinedPasswordTextField)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            CoreEnum.AuthenticationOptions.LOGIN.option -> {
                CoreEnum.AuthenticationOptions.LOGIN.option
            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                CoreEnum.AuthenticationOptions.SIGN_UP.option
            }

            else -> {
                CoreEnum.AuthenticationOptions.LOGIN.option
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View
        when (viewType) {
            CoreEnum.AuthenticationOptions.LOGIN.option -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_authentication_login, parent, false)
                return LoginViewHolder(view)
            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_authentication_signup, parent, false)
                return SignUpViewHolder(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_authentication_login, parent, false)
                return LoginViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CoreEnum.AuthenticationOptions.LOGIN.option -> {
                val loginViewHolder = holder as LoginViewHolder

                loginViewHolder.etEditTextEmailUsername.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                            loginViewHolder.tilOutlinedLoginEmailTextField.error = mContext.getString(R.string.auth_activity_sign_up_email_error_text)
                            mListener.insertEmailAddress(s.toString(), false)
                        }
                        else {
                            loginViewHolder.tilOutlinedLoginEmailTextField.error = null
                            mListener.insertEmailAddress(s.toString(), true)
                        }
                    }
                })

                loginViewHolder.etEditTextPassword.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        mListener.insertPassword(s.toString(), true)
                    }
                })
            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                val  signupViewHolder = holder as SignUpViewHolder

                val specialCharacterRegex = "[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]"
                val letterRegex = "[a-zA-Z]"
                val numberRegex = "\\d"

                signupViewHolder.etEditTextPassword.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        if (!s.contains(Regex(specialCharacterRegex)) || !s.contains(Regex(letterRegex)) || !s.contains(Regex(numberRegex)) || s.length < 8) {
                            signupViewHolder.tilOutlinedPasswordTextField.error = mContext.getString(R.string.auth_activity_sign_up_password_error_text)
                            mListener.insertPassword(s.toString(), false)
                        }
                        else {
                            signupViewHolder.tilOutlinedPasswordTextField.error = null
                            mListener.insertPassword(s.toString(), true)
                        }
                    }
                })

                signupViewHolder.etEditTextEmail.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                            signupViewHolder.tilOutlinedSignUpEmailTextField.error = mContext.getString(R.string.auth_activity_sign_up_email_error_text)
                            mListener.insertEmailAddress(s.toString(), false)
                        }
                        else {
                            signupViewHolder.tilOutlinedSignUpEmailTextField.error = null
                            mListener.insertEmailAddress(s.toString(), true)
                        }
                    }
                })
            }
        }
    }
}