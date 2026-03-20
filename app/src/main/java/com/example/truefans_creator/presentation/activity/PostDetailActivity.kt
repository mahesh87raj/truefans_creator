package com.example.truefans_creator.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.truefans_creator.presentation.adapter.DetailPostAdapter
import com.example.truefans_creator.data.repository.CreatorRepository
import com.example.truefans_creator.data.local.ProfileManager
import com.example.truefans_creator.databinding.ActivityPostDetailBinding
import com.example.truefans_creator.domain.model.CreatorPost
import com.example.truefans_creator.domain.model.CreatorProfile

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var profileManager: ProfileManager
    private var detailPostAdapter: DetailPostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileManager = ProfileManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val clickedPost = intent.getParcelableExtra<CreatorPost>("POST_DATA")
        val allPosts = CreatorRepository.getCreatorPosts()
        
        // Get the latest profile from SharedPreferences
        val profile = profileManager.getProfile() ?: CreatorRepository.getCreatorProfile()
        
        // Find the position of the clicked post to scroll to it
        val initialPosition = allPosts.indexOfFirst { it.id == clickedPost?.id }.coerceAtLeast(0)

        setupRecyclerView(allPosts, profile, initialPosition)
    }

    override fun onResume() {
        super.onResume()
        // Refresh profile data in case it was changed (e.g. name or avatar)
        val latestProfile = profileManager.getProfile() ?: CreatorRepository.getCreatorProfile()
        detailPostAdapter?.updateProfile(latestProfile)
    }

    private fun setupRecyclerView(posts: List<CreatorPost>, profile: CreatorProfile, position: Int) {
        detailPostAdapter = DetailPostAdapter(posts, profile)
        binding.rvDetailPosts.apply {
            layoutManager = LinearLayoutManager(this@PostDetailActivity)
            adapter = detailPostAdapter
            // Scroll to the post that was clicked in the grid
            scrollToPosition(position)
        }
    }
}
