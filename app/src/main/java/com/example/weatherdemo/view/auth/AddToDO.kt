package com.example.weatherdemo.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherdemo.MainActivity
import com.example.weatherdemo.R
import com.example.weatherdemo.databinding.ActivityAddToDoBinding

class AddToDO : AppCompatActivity() {

    private lateinit var binding: ActivityAddToDoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddToDoBinding.inflate(layoutInflater)


        binding.btnBack.setOnClickListener {
            val intent = Intent(this,   MainActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)

    }
}