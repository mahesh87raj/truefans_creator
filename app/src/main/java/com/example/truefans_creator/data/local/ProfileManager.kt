package com.example.truefans_creator.data.local

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.example.truefans_creator.domain.model.CreatorProfile
import java.io.File
import java.io.FileOutputStream

class ProfileManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("creator_prefs", Context.MODE_PRIVATE)

    fun saveProfile(profile: CreatorProfile) {
        prefs.edit().apply {
            putString("name", profile.name)
            putString("bio", profile.bio)
            putInt("followers", profile.followers)
            putString("avatarUrl", profile.avatarUrl)
            putBoolean("isFollowing", profile.isFollowing)
            apply()
        }
    }

    fun getProfile(): CreatorProfile? {
        val name = prefs.getString("name", null) ?: return null
        val bio = prefs.getString("bio", "") ?: ""
        val followers = prefs.getInt("followers", 1200)
        var avatarUrl = prefs.getString("avatarUrl", "") ?: ""
        val isFollowing = prefs.getBoolean("isFollowing", false)
        
        // Migrate content URIs to internal storage if found during retrieval
        if (avatarUrl.startsWith("content://")) {
            val internalPath = saveImageToInternalStorage(Uri.parse(avatarUrl))
            if (internalPath != null) {
                avatarUrl = internalPath
                // Update SharedPreferences with the new persistent path
                prefs.edit().putString("avatarUrl", internalPath).apply()
            }
        }
        
        return CreatorProfile(name, bio, followers, avatarUrl, isFollowing)
    }

    fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile_picture.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
