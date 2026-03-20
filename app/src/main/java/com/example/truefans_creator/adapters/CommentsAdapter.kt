package com.example.truefans_creator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.truefans_creator.databinding.ItemCommentBinding
import com.example.truefans_creator.models.Comment

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
    }

    override fun getItemCount(): Int = comments.size

    fun addComment(comment: Comment) {
        comments.add(0, comment)
        notifyItemInserted(0)
    }
}