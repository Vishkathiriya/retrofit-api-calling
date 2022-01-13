package com.example.ibl_post_call

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ibl_post_call.repsonse.MyResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jsonParser = JsonParser()
        val gson = Gson()

        btnSubmit.setOnClickListener {
            val map = HashMap<String, String>()
            map["name"] = tvName?.text.toString()
            map["salary"] = tvSalary?.text.toString()
            map["age"] = tvAge?.text.toString()

            //convert map to JsonObject
            postData(jsonParser.parse(gson.toJson(map)) as JsonObject)
        }
    }

    private fun postData(jsonObject: JsonObject) {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://dummy.restapiexample.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(MyInterface::class.java)

        val call = apiService.createUser(jsonObject)

        call.enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                tvResponse?.text = response.body().toString()
                Log.e("=====Success======", response.message())
            }

            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("======Error========", t.localizedMessage)
            }
        })
    }

}