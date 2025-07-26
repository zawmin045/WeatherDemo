package com.example.weatherdemo.view.auth

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherdemo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        binding.btnsignUp.setOnClickListener {

            val username = binding.etUsername.editText!!.text.toString()
            val email = binding.etEmail.editText!!.text.toString()
            val password = binding.etPassword.editText!!.text.toString()
            val confirmPassword = binding.etConfirmPassword.editText!!.text.toString()

            when {
                TextUtils.isEmpty(username) -> {
                    binding.etUsername.error = "Username is required"
                    binding.etUsername.requestFocus()
                }

                TextUtils.isEmpty(email) -> {
                    binding.etEmail.error = "Email is required"
                    binding.etEmail.requestFocus()
                }

                TextUtils.isEmpty(password) -> {
                    binding.etPassword.error = "Password is required"
                    binding.etPassword.requestFocus()
                }

                TextUtils.isEmpty(confirmPassword) -> {
                    binding.etConfirmPassword.error = "Confirm Password is required"
                    binding.etConfirmPassword.requestFocus()
                }

                password != confirmPassword -> {
                    binding.etConfirmPassword.error = "Passwords do not match"
                    binding.etConfirmPassword.requestFocus()
                }

                else -> {
                    // Proceed with sign-up logic
                    registerAccount(username, email, password, confirmPassword)
                    Log.d("SignUp", "Username: $username, Email: $email, Password: $password")
                }
            }
        }

//============================================
        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
//============================================
        setContentView(binding.root)

    }

    fun registerAccount(username: String, email: String, password: String, confirmPassword: String) {

        if (checkInternetConnection(this)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    run {
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "Registration failed: ", Toast.LENGTH_SHORT).show()
                            Log.e("SignUp", "Registration failed", task.exception)
                        }


                    }
        }
    }else
    {
        Toast.makeText(this, "Please chack internet connection", Toast.LENGTH_SHORT).show()
    }

}


        fun checkInternetConnection(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }


}


