package com.example.listdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    var myList: List<Post> = emptyList()

    var postClickListener: PostClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = myList[position]
        holder.bindPost(item)
    }

    inner class PostViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindPost(item: Post) {
            view.findViewById<TextView>(R.id.txtTitle).text = item.title
            view.setOnClickListener {
                postClickListener?.onPostClicked(item)
            }
        }
    }
}

interface PostClickListener {
    fun onPostClicked(post: Post)
}