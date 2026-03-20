package com.example.truefans_creator.data

import com.example.truefans_creator.R
import com.example.truefans_creator.models.CreatorPost
import com.example.truefans_creator.models.CreatorProfile
import kotlin.random.Random

object CreatorRepository {
    fun getCreatorProfile(): CreatorProfile {
        return CreatorProfile(
            name = "Virat Kohli",
            bio = "Indian Cricketer | Passionate about fitness, family, and the game. Let's win together! 🏏🇮🇳",
            followers = 1200,
            avatarUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6-mJ_D2T4_y2eI_l4P7Zz-N-Z1S6N3N4W4w&s"
        )
    }

    fun getCreatorPosts(): List<CreatorPost> {
        val drawables = listOf(
            R.drawable.arm,
            R.drawable.army,
            R.drawable.env,
            R.drawable.envi,
            R.drawable.ew,
            R.drawable.enviro,
            R.drawable.fighter,
            R.drawable.fl,
            R.drawable.flow,
            R.drawable.img,
            R.drawable.ln,
            R.drawable.ms,
            R.drawable.rohit,
            R.drawable.w,
            R.drawable.wer,
            R.drawable.wtr,
            R.drawable.city,
            R.drawable.cy,
            R.drawable.ele,
            R.drawable.lion,
            R.drawable.lembo,
        )
        
        return drawables.mapIndexed { index, resId ->
            CreatorPost(
                id = index.toString(),
                thumbnailResId = resId,
                likeCount = Random.nextInt(1000, 50000),
                isLiked = false,
                caption = "Passion, hard work, and the right mindset. 🏏 #Cricket #Motivation #ViratKohli"
            )
        }
    }
}