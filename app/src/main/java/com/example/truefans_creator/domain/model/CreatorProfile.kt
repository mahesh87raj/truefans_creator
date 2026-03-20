package com.example.truefans_creator.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatorProfile(
    val name: String,
    val bio: String,
    var followers: Int,
    val avatarUrl: String,
    var isFollowing: Boolean = false
) : Parcelable

@Parcelize
data class CreatorPost(
    val id: String,
    val thumbnailResId: Int,
    var likeCount: Int = 0,
    var isLiked: Boolean = false,
    val caption: String = ""
) : Parcelable
