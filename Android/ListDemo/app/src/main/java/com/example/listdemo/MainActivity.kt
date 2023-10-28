package com.example.listdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.listdemo.databinding.ActivityMainBinding
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*val jsonObject = JSONObject()
        jsonObject.get("id")*/

        var adapter = MyListAdapter()
        findViewById<RecyclerView>(R.id.rv_my_list).adapter = adapter

        val list = listOf<MyListItem>(
            MyListItem.Post(R.drawable.rectangle, "item without border"),
            MyListItem.Post(R.drawable.rectangle, "item without border"),
            MyListItem.Post(R.drawable.rectangle_with_border, "item", url),
            MyListItem.Title("item without border"),
            MyListItem.Post(R.drawable.rectangle_with_border, "item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Post(R.drawable.rectangle_with_border, "item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Post(R.drawable.rectangle_with_border, "item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Post(R.drawable.rectangle_with_border, "item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Title("item without border"),
            MyListItem.Post(R.drawable.rectangle_with_border, "item without border"),
            MyListItem.Title("item without border")
        )
        adapter.myList = (list)
        adapter.notifyDataSetChanged()

        makeApiCall()
    }

    fun makeApiCall() {
        val client = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()

        val api = client.create(FakeApi::class.java)

        val getPostsCall = api.getPost(1)

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

val url =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsJSFvewBphCd0-gaP5jDukdNiNsEaaiOnYA&usqp=CAU"

interface FakeApi {
    @GET("/posts")
    fun getPots(): Call<List<Post>>


    @GET("/posts/{id}")
    fun getPost(
        @Path("id")
        id: Int
    ): Call<Post>

    @GET("/comments")
    fun getPostComments(
        @Query("postId")
        id: Int
    ): Call<List<Comment>>
}

data class Post(
    val id: Int,
    val userId: Int,
    val title: String
)

data class Comment(
    @SerializedName("postId")
    val id: Int
)