package com.example.listdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyListAdapter : RecyclerView.Adapter<MyListAdapter.MyViewHolder>() {

    private val VIEW_TYPE_POST = 1
    private val VIEW_TYPE_TITLE = 2


    var myList: List<MyListItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    when (viewType) {
                        1 -> R.layout.item_my_list
                        else -> R.layout.item_my_list_title
                    }, parent, false
                )
        )
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myList[position]
        when (item) {
            is MyListItem.Post -> holder.bindPost(item)
            is MyListItem.Title -> holder.bindTitle(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (myList[position]) {
            is MyListItem.Post -> VIEW_TYPE_POST
            else -> VIEW_TYPE_TITLE
        }
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindPost(item: MyListItem.Post) {
            view.findViewById<TextView>(R.id.txtTitle).text = item.title
            item.imageUrl?.let { url ->
                Glide.with(view.context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(view.findViewById(R.id.imageView))
            } ?: run {
                view.findViewById<ImageView>(R.id.imageView)
                    .setImageBitmap(null)
            }
        }

        fun bindTitle(item: MyListItem.Title) {
            view.findViewById<TextView>(R.id.txtTitle).text = item.title
        }
    }
}

interface MyListItem {
    data class Post(val imageId: Int, val title: String, val imageUrl: String? = null) : MyListItem
    data class Title(val title: String) : MyListItem
}


fun View.isNotVisible(): Boolean {
    return false
}