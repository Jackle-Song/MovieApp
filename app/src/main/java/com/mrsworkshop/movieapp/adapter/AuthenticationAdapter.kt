package com.mrsworkshop.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.mrsworkshop.movieapp.R
import com.mrsworkshop.movieapp.core.CoreEnum

class AuthenticationAdapter(
    private val mContext : Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class LoginViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etEditTextEmailUsername : EditText = itemView.findViewById(R.id.etEditTextEmailUsername)
        val etEditTextPassword : EditText = itemView.findViewById(R.id.etEditTextPassword)
    }

    class SignUpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

            }

            CoreEnum.AuthenticationOptions.SIGN_UP.option -> {
                val  signupViewHolder = holder as SignUpViewHolder
            }
        }
    }
}