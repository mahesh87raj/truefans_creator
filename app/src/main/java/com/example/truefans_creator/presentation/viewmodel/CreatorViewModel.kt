package com.example.truefans_creator.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.truefans_creator.data.local.AppDatabase
import com.example.truefans_creator.data.local.ProfileManager
import com.example.truefans_creator.data.repository.CreatorRepository
import com.example.truefans_creator.domain.model.CreatorPost
import com.example.truefans_creator.domain.model.CreatorProfile
import com.example.truefans_creator.domain.model.Transaction
import kotlinx.coroutines.launch

class CreatorViewModel(application: Application) : AndroidViewModel(application) {

    private val profileManager = ProfileManager(application)
    private val transactionDao = AppDatabase.getDatabase(application).transactionDao()
    
    val creator = MutableLiveData<CreatorProfile>()
    val posts = MutableLiveData<List<CreatorPost>>()
    val transactions = MutableLiveData<List<Transaction>>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        loadCreatorData()
    }

    fun loadCreatorData() {
        isLoading.value = true
        
        // Try to get from SharedPreferences first
        var profile = profileManager.getProfile()
        
        if (profile == null) {
            // First time: Get mock data from Repository and save to Prefs
            profile = CreatorRepository.getCreatorProfile()
            profile.followers = 1200
            profile.isFollowing = false
            profileManager.saveProfile(profile)
        }
        
        creator.value = profile
        posts.value = CreatorRepository.getCreatorPosts()
        isLoading.value = false
        
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            transactions.value = transactionDao.getAllTransactions()
        }
    }

    fun toggleFollow() {
        val current = creator.value!!
        current.isFollowing = !current.isFollowing
        current.followers += if (current.isFollowing) 1 else -1
        
        // Update SharedPreferences
        profileManager.saveProfile(current)
        
        creator.value = current
    }

    fun updateProfile(newName: String, newBio: String, newAvatarUrl: String) {
        val current = creator.value!!
        val updated = current.copy(name = newName, bio = newBio, avatarUrl = newAvatarUrl)
        
        profileManager.saveProfile(updated)
        creator.value = updated
    }
}
