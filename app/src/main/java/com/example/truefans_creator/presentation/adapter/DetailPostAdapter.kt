package com.example.truefans_creator.presentation.adapter

import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.truefans_creator.R
import com.example.truefans_creator.databinding.ItemDetailPostBinding
import com.example.truefans_creator.databinding.LayoutCommentsBottomSheetBinding
import com.example.truefans_creator.domain.model.Comment
import com.example.truefans_creator.domain.model.CreatorPost
import com.example.truefans_creator.domain.model.CreatorProfile
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class DetailPostAdapter(
    private val posts: List<CreatorPost>,
    private var profile: CreatorProfile
) : RecyclerView.Adapter<DetailPostAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemDetailPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun updateProfile(newProfile: CreatorProfile) {
        this.profile = newProfile
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        holder.binding.tvCreatorName.text = profile.name

        // Handle avatar with cache invalidation using file timestamp
        val avatarFile = File(profile.avatarUrl)
        val signature = if (avatarFile.exists()) avatarFile.lastModified().toString() else profile.avatarUrl

        val glide = Glide.with(holder.itemView.context)

        // Main Header Avatar
        glide.load(profile.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .signature(ObjectKey(signature))
            .circleCrop()
            .into(holder.binding.ivDetailAvatar)

        // "Liked by" Avatar section
        glide.load(profile.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .signature(ObjectKey(signature))
            .circleCrop()
            .into(holder.binding.ivLikeProfile)

        glide.load(post.thumbnailResId)
            .into(holder.binding.ivPostImage)

        updateLikeUI(holder, post)

        holder.binding.ivLike.setOnClickListener {
            post.isLiked = !post.isLiked
            post.likeCount += if (post.isLiked) 1 else -1
            updateLikeUI(holder, post)
        }

        holder.binding.ivComment.setOnClickListener {
            showCommentsBottomSheet(holder, post, signature)
        }

        holder.binding.ivShare.setOnClickListener {
            sharePost(holder, post)
        }
    }

    private fun updateLikeUI(holder: ViewHolder, post: CreatorPost) {
        val context = holder.itemView.context
        if (post.isLiked) {
            holder.binding.ivLike.setImageResource(android.R.drawable.btn_star_big_on)
            holder.binding.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.primary))
        } else {
            holder.binding.ivLike.setImageResource(android.R.drawable.btn_star_big_off)
            holder.binding.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black))
        }

        val formattedLikes = NumberFormat.getNumberInstance(Locale.US).format(post.likeCount)

        // 1. Update "Liked by" text with Bold Name
        val likesText = "Liked by ${profile.name} and $formattedLikes others"
        val likesSpannable = SpannableStringBuilder(likesText)
        val nameStart = likesText.indexOf(profile.name)
        if (nameStart != -1) {
            likesSpannable.setSpan(StyleSpan(Typeface.BOLD), nameStart, nameStart + profile.name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.binding.tvLikes.text = likesSpannable

        // 2. Update Caption with Bold Name
        val captionText = "${profile.name} ${post.caption}"
        val captionSpannable = SpannableStringBuilder(captionText)
        if (captionText.startsWith(profile.name)) {
            captionSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, profile.name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.binding.tvCaption.text = captionSpannable
    }

    private fun showCommentsBottomSheet(holder: ViewHolder, post: CreatorPost, avatarSignature: String) {
        val context = holder.itemView.context
        val dialog = BottomSheetDialog(context)
        val bottomSheetBinding = LayoutCommentsBottomSheetBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bottomSheetBinding.root)

        Glide.with(context)
            .load(profile.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .signature(ObjectKey(avatarSignature))
            .circleCrop()
            .into(bottomSheetBinding.ivCommentUserAvatar)

        val comments = mutableListOf(
            Comment("Rohit Sharma", "Great shot!", "2h", ""),
            Comment("MS Dhoni", "Keep it up.", "5h", ""),
            Comment("Hardik Pandya", "Legend! 🔥", "1h", "")
        )

        val adapter = CommentsAdapter(comments)
        bottomSheetBinding.rvComments.layoutManager = LinearLayoutManager(context)
        bottomSheetBinding.rvComments.adapter = adapter

        bottomSheetBinding.tvPostComment.setOnClickListener {
            val commentText = bottomSheetBinding.etComment.text.toString()
            if (commentText.isNotEmpty()) {
                adapter.addComment(Comment(profile.name, commentText, "now", profile.avatarUrl))
                bottomSheetBinding.etComment.text.clear()
            }
        }

        dialog.show()
    }

    private fun sharePost(holder: ViewHolder, post: CreatorPost) {
        val context = holder.itemView.context
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this post by ${profile.name} on TrueFans! 🏏\n${post.caption}")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share post via"))
    }

    override fun getItemCount(): Int = posts.size
}
