package com.example.weatherdemo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherdemo.adapter.MyHourlyAdapter
import com.example.weatherdemo.adapter.MyWeeklyAdapter
import com.example.weatherdemo.databinding.ActivityMainBinding
import com.example.weatherdemo.model.MyHourly
import com.example.weatherdemo.model.MyWeekly
import com.example.weatherdemo.model.Weather
import com.example.weatherdemo.network.ApiInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import kotlin.collections.isNotEmpty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var hrList: ArrayList<MyHourly>
    private lateinit var wkList: ArrayList<MyWeekly>

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if(location != null){
                val latitude = location.latitude
                val longitude = location.longitude
                val cityName = getTownship(latitude, longitude)
                runOnUiThread {
                    binding.txtTownShip.text = cityName.toString()
                }
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude, City: $cityName")
                Toast.makeText(this, "Lat: $latitude, Lon: $longitude", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
            }
        }

        fetchWeatherData()

        setContentView(binding.root)



    }

    private fun getTownship(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].adminArea
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun fetchWeatherData(){
        hrList = arrayListOf()
        wkList = arrayListOf()
        // Step 1 Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/" ) // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Call getWeather
        val apiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiInterface.getWeather(
            latitude = 52.52, // Replace with actual latitude
            longitude = 13.41, // Replace with actual longitude
            daily = "temperature_2m_max,temperature_2m_min,rain_sum",
            hourly = "temperature_2m,rain",
            current = "temperature_2m,is_day,wind_speed_10m,wind_direction_10m,wind_gusts_10m"
        )

        // success or fail
        call.enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    if (weather != null) {
                        // Handle the weather data
                        Toast.makeText(this@MainActivity, "Weather data fetched successfully", Toast.LENGTH_SHORT).show()
                        Log.d("Weather",weather.toString())
                        var todayTemperature = weather.current.temperature_2m.toString()+weather.current_units.temperature_2m.toString()
                        Log.d("Weather", "Current Temperature: $todayTemperature")
                        binding.txtCurrentTemp.text = todayTemperature

                        //Max Min Current day
                        val maxTmp = weather.daily.temperature_2m_max[0].toString()
                        val minTmp = weather.daily.temperature_2m_min[0].toString()
                        binding.txtCurrentMaxMin.text = "$maxTmp° / $minTmp° Feels like $todayTemperature°"

                        //Wind Speed
                        val windSpeed = weather.current.wind_speed_10m.toString() + weather.current_units.wind_speed_10m.toString()
                        binding.txtWindSpeed.text = windSpeed

                        for(i in 0..23){
                            val time = weather.hourly.time[i].substringAfter("T")
                            val tmp = weather.hourly.temperature_2m[i].toString() + weather.hourly_units.temperature_2m
                            val rain = weather.hourly.rain[i].toString()
                            val hourlyData = MyHourly(time, tmp, rain)
                            hrList.add(hourlyData)

                            if( i <= 6){
                                var wkDate = weather.daily.time[i].toString()
                                var wkRain = weather.daily.rain_sum[i].toString()
                                var wkMaxTmp = weather.daily.temperature_2m_max[i].toString()
                                var wkMinTmp = weather.daily.temperature_2m_min[i].toString()
                                val wkData = MyWeekly(wkDate, wkMaxTmp, wkMinTmp, wkRain)
                                wkList.add(wkData)
                            }


                        }
                        // Update RecyclerView adapter
                        val adapter = MyHourlyAdapter(this@MainActivity, hrList)
                        val wkAdapter = MyWeeklyAdapter(this@MainActivity, wkList)
                        runOnUiThread {
                            binding.rvHourly.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                            binding.rvHourly.adapter = adapter
                            binding.rvWeekly.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                            binding.rvWeekly.adapter = wkAdapter
                        }


                    } else {
                        Toast.makeText(this@MainActivity, "No weather data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch weather data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("WeatherError", "Error fetching weather data: ${t.message} , ${t.localizedMessage}")
            }
        })

    }




}