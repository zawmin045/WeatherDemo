package com.example.weatherdemo.view.auth

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherdemo.MainActivity
import com.example.weatherdemo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        val shp = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val info = shp.getBoolean("saveLogin", false)

        if (info){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the Login activity
        }

        binding.btnLogin.setOnClickListener {
            var email = binding.etEmail.text.toString()
            var password = binding.etPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                binding.etEmail.error = "Email is required"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password)) {
                binding.etPassword.error = "Password is required"
                return@setOnClickListener
            } else {
                // Login code here, e.g., authenticate user
                loginAccount(email, password)
            }

        }
        //========================================= စကရင်ကူခြင်းကို Edge to Edge လုပ်ရန်


        binding.txtForget.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)

        }
        //========================================



        setContentView(binding.root)
    }

    fun loginAccount(email: String, password: String) {
    // Check internet connection before proceeding with login
        if (binding.chLoginSave.isEnabled) {
            val sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("saveLogin", true)
            editor.commit()
        }
    // Check if the device has an internet connection
        if (checkInternetConnection(this)){
            //logic to authenticate user

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val intent = Intent (this , MainActivity ::class.java)
                    startActivity(intent)


                }else{
                    Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show()

                }

            }
    }else {
        Toast.makeText(this,"Please check your internet connection", Toast.LENGTH_SHORT).show()
    }
}

    fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }

}