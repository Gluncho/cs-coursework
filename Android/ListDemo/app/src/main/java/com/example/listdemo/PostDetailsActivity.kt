package com.example.listdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        val id = intent.getIntExtra("postId", 0)
        getPostDetails(id)

        Log.d("lifecycle", "onCreate")

        findViewById<TextView>(R.id.textView).setOnClickListener {
            setResult(100, Intent().apply {
                putExtra("result", "result from details")
            })

            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d("lifecycle", "onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("lifecycle", "onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("lifecycle", "onPause")
    }


    override fun onStop() {
        super.onStop()

        Log.d("lifecycle", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("lifecycle", "onDestroy")
    }

    fun getPostDetails(id: Int) {
        val client = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()

        val api = client.create(FakeApi::class.java)

        val getPostsCall = api.getPost(id)

        getPostsCall.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {

                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("fail", "api fail")
            }

        })
    }
}