package com.example.truefans_creator.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.truefans_creator.PostDetailActivity
import com.example.truefans_creator.databinding.ItemPostBinding
import com.example.truefans_creator.models.CreatorPost

class PostAdapter(private var posts: List<CreatorPost>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    fun updatePosts(newPosts: List<CreatorPost>) {
        this.posts = newPosts
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.binding.postThumbnail.post {
            val width = holder.binding.postThumbnail.width
            holder.binding.postThumbnail.layoutParams.height = width
            holder.binding.postThumbnail.requestLayout()
        }

        Glide.with(holder.itemView.context)
            .load(post.thumbnailResId)
            .centerCrop()
            .into(holder.binding.postThumbnail)

        // Open Detail Page on Click (Instagram style)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PostDetailActivity::class.java).apply {
                putExtra("POST_DATA", post)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = posts.size
}