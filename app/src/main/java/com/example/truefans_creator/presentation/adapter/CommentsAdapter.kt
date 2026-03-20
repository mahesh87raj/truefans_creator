package com.example.truefans_creator.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.truefans_creator.R
import com.example.truefans_creator.databinding.ItemCommentBinding
import com.example.truefans_creator.domain.model.Comment
import java.io.File

class CommentsAdapter(private val comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.binding.tvUserName.text = comment.userName
        holder.binding.tvCommentText.text = comment.text
        holder.binding.tvCommentTime.text = comment.time

        // Handle profile picture for the comment
        val avatarUrl = comment.userAvatarUrl
        val avatarFile = File(avatarUrl)
        val signature = if (avatarFile.exists()) avatarFile.lastModified().toString() else avatarUrl

        Glide.with(holder.itemView.context)
            .load(if (avatarUrl.isNotEmpty()) avatarUrl else R.drawable.ic_launcher_background)
            .placeholder(R.drawable.ic_launcher_background)
            .signature(ObjectKey(signature))
            .circleCrop()
            .into(holder.binding.ivCommentAvatar)
    }

    override fun getItemCount(): Int = comments.size

    fun addComment(comment: Comment) {
        comments.add(0, comment)
        notifyItemInserted(0)
    }
}
