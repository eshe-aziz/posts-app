package com.akirachix.postsapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class PostAdapter (var post: List<Post>, val context: Context):
    RecyclerView.Adapter<PostAppViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAppViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_view,parent,false)
        return PostAppViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: PostAppViewHolder, position: Int) {
        val post=post[position]
        holder.body.text = post.body
        holder.title.text = post.title
        holder.id.text="ID:${post.id}"
        holder.userId.text="UserId:${post.userId}"
        holder.clPost.setOnClickListener {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra("POST_ID", post.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return post.size
    }
}

class PostAppViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var body=itemView.findViewById<TextView>(R.id.tvBody)
    var title=itemView.findViewById<TextView>(R.id.tvTitle)
    var id=itemView.findViewById<TextView>(R.id.tvID)
    var userId=itemView.findViewById<TextView>(R.id.tvUserID)
    var clPost=itemView.findViewById<MaterialCardView>(R.id.clPost)
}