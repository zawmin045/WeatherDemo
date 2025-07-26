package com.example.weatherdemo

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherdemo.databinding.ActivityLoginBinding
import com.example.weatherdemo.databinding.ActivityMainBinding
import com.example.weatherdemo.view.auth.Account
import com.example.weatherdemo.view.auth.AddToDO

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
             isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
                // Permission is granted, proceed with location access

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        //Check user Allow permissions for location
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }



        //========================================= စကရင်ကူခြင်းကို Edge to Edge လုပ်ရန်


           binding.btnToDO.setOnClickListener {
               val intent = Intent(this, AddToDO::class.java)
               startActivity(intent)
           }

        binding.btnzaw.setOnClickListener {
            val intent = Intent(this,Account::class.java)
            startActivity(intent)
        }


       
    }

}