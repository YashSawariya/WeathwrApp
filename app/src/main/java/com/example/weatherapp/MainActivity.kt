package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var codeo = 101                               // create unique code of device
    lateinit var flpc: FusedLocationProviderClient          //create reference variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        flpc = LocationServices.getFusedLocationProviderClient(this)   //enitilize this

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {  //check permission are grandet or not
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                codeo
            )       //give request to granted permission
        } else {
            getloc()
        }


//
//        val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/").addConverterFactory(GsonConverterFactory.create()).build()
//        val api:ApiInterface=retrofit.create(ApiInterface::class.java)
//        val item=api.getCurrentWeather(citing,"f7056f1fc2f5a7965b8392d45cb47970")
//        item.enqueue(object :Callback<Item>{
//            @SuppressLint("SetTextI18n")
//            override fun onResponse(call: Call<Item>, response: Response<Item>) {
//                  val aa:Item=response.body()!!
//                  todayrep.text=aa.weather[0].description
//                address.text = "${aa.sys.country}, $citing"
//                temp.text = "${aa.main.temp}°"
//                temp2.text = "H ${aa.main.temp_max} L ${aa.main.temp_min}"
//
//            }
//
//            override fun onFailure(call: Call<Item>, t: Throwable) {
//                Toast.makeText(this@MainActivity,"Fail",Toast.LENGTH_LONG).show()
//            }
//        })
    }

    @SuppressLint("MissingPermission")
    fun getloc() {
        val pl = flpc.getLastLocation()                               //get current or last location
        pl.addOnSuccessListener {
            if (it != null) {

                val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
        val api:ApiInterface=retrofit.create(ApiInterface::class.java)
        val item=api.getCurrentWeather(it.latitude.toString(),it.longitude.toString(),"f7056f1fc2f5a7965b8392d45cb47970")
        item.enqueue(object :Callback<Item>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                  val aa:Item=response.body()!!
                  todayrep.text=aa.weather[0].description
                address.text = "${aa.sys.country}"
                temp.text = "${aa.main.temp}°"
                temp2.text = "H ${aa.main.temp_max} L ${aa.main.temp_min}"

            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Fail",Toast.LENGTH_LONG).show()
            }
        })
            } else {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }
        }
        pl.addOnFailureListener {
            Toast.makeText(this, "Faliar", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {             //this mathod helps to granted the permission
        if (requestCode == codeo) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getloc()
            }
        }

    }
}