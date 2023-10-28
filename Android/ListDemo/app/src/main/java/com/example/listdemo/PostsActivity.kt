package com.example.listdemo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostsActivity : AppCompatActivity() {

    val adapter = PostsAdapter()

    var postDetailsActivityContract = PostDetailsActivityContract()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)


        registerForActivityResult(postDetailsActivityContract) {

        }.launch(1)



        adapter.postClickListener = object : PostClickListener {
            override fun onPostClicked(post: Post) {
                val intent = Intent(
                    this@PostsActivity,
                    PostDetailsActivity::class.java
                ).apply {
                    putExtra("postId", post.id)
                }
                startActivity(intent)
            }

        }
        findViewById<RecyclerView>(R.id.rv_posts).adapter = adapter


        getPosts()

        Log.d("lifecycle", "onCreate")
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


    fun getPosts() {
        val client = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()

        val api = client.create(FakeApi::class.java)

        val getPostsCall = api.getPots()

        getPostsCall.enqueue(object : Callback<List<Post>> {
            override fun onResponse(
                call: Call<List<Post>>,
                response: Response<List<Post>>
            ) {
                if (response.isSuccessful) {
                    adapter.myList = response.body()?.toList() ?: emptyList()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {

            }
        })
    }

}

class PostDetailsActivityContract : ActivityResultContract<Int, String>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, PostDetailsActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return intent?.getStringExtra("result") ?: ""
    }

}