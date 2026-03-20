package com.example.truefans_creator.presentation.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.truefans_creator.R
import com.example.truefans_creator.SupportCreatorActivity
import com.example.truefans_creator.databinding.ActivityMainBinding
import com.example.truefans_creator.domain.model.CreatorProfile
import com.example.truefans_creator.presentation.adapter.PostAdapter
import com.example.truefans_creator.presentation.viewmodel.CreatorViewModel
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CreatorViewModel by viewModels()
    private var postAdapter: PostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCreatorData()
    }

    private fun setupViews() {
        postAdapter = PostAdapter(emptyList())
        binding.rvPosts.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = postAdapter
        }
        
        binding.btnFollow.setOnClickListener {
            viewModel.toggleFollow()
        }

        binding.btnTip.setOnClickListener {
            startActivity(Intent(this, SupportCreatorActivity::class.java))
        }

        binding.ivEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.creator.observe(this) { profile ->
            updateUI(profile)
        }

        viewModel.posts.observe(this) { posts ->
            postAdapter?.updatePosts(posts)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvPosts.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun updateUI(profile: CreatorProfile) {
        binding.tvName.text = profile.name
        binding.tvBio.text = profile.bio
        binding.tvFollowerCount.text = formatFollowers(profile.followers)
        binding.tvAvatarUrl.text = profile.avatarUrl
        
        if (profile.isFollowing) {
            binding.btnFollow.text = "Following"
            binding.btnFollow.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.light_gray)
            )
            binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            binding.btnFollow.text = "Follow"
            binding.btnFollow.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.primary)
            )
            binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        val avatarFile = File(profile.avatarUrl)
        val signature = if (avatarFile.exists()) avatarFile.lastModified().toString() else profile.avatarUrl

        Glide.with(this)
            .load(profile.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .signature(ObjectKey(signature))
            .circleCrop()
            .into(binding.ivAvatar)
    }

    private fun formatFollowers(count: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(count)
    }
}
