package com.example.truefans_creator.presentation.activity

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.truefans_creator.R
import com.example.truefans_creator.data.local.ProfileManager
import com.example.truefans_creator.databinding.ActivityEditProfileBinding
import com.example.truefans_creator.domain.model.CreatorProfile

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var profileManager: ProfileManager
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // Save to internal storage immediately to get the persistent path
            val persistentPath = profileManager.saveImageToInternalStorage(it)
            if (persistentPath != null) {
                // Update the text field with the actual saved path
                binding.etAvatarUrl.setText(persistentPath)
                updateImagePreview(persistentPath)
            } else {
                binding.etAvatarUrl.setText(it.toString())
                updateImagePreview(it.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileManager = ProfileManager(this)
        
        setupToolbar()
        setupInitialData()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.editToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Profile"
        binding.editToolbar.setNavigationOnClickListener { 
            onBackPressedDispatcher.onBackPressed() 
        }
    }

    private fun setupInitialData() {
        val profile = profileManager.getProfile() ?: return
        
        binding.etName.setText(profile.name)
        binding.etBio.setText(profile.bio)
        binding.etAvatarUrl.setText(profile.avatarUrl)
        
        updateImagePreview(profile.avatarUrl)
    }

    private fun updateImagePreview(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background)
            .circleCrop()
            .into(binding.ivEditAvatar)
    }

    private fun setupListeners() {
        // Update preview if user manually types/pastes a URL
        binding.etAvatarUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (selectedImageUri == null) { // Only if we didn't just pick an image
                    updateImagePreview(s.toString())
                }
            }
        })

        binding.btnSaveProfile.setOnClickListener {
            val newName = binding.etName.text.toString()
            val newBio = binding.etBio.text.toString()
            val newUrl = binding.etAvatarUrl.text.toString()

            if (newName.isNotEmpty()) {
                val profile = profileManager.getProfile()!!
                
                // Save the text currently in the etAvatarUrl field
                val updatedProfile = profile.copy(
                    name = newName,
                    bio = newBio,
                    avatarUrl = newUrl 
                )
                
                profileManager.saveProfile(updatedProfile)
                Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvChangeAvatar.setOnClickListener {
            selectedImageUri = null // Reset before picking
            pickImageLauncher.launch("image/*")
        }

        binding.ivEditAvatar.setOnClickListener {
            selectedImageUri = null // Reset before picking
            pickImageLauncher.launch("image/*")
        }
    }
}
