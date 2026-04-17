package ru.netology.nmedia.dto

import ru.netology.nmedia.R

data class Post(
    val id: Int = 0,
    val author: String = "",
    val authorAvatar: Int = R.drawable.ic_netology_logo_48,
    val published: String = "",
    val content: String = "",
    val likeCount: Int = 0,
    val repostCount: Int = 0,
    val viewCount: Int = 0,
    val likedByMe: Boolean = false,
    val video: String? = ""
)
