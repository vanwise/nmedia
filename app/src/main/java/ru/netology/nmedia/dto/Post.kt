package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val authorAvatar: Int,
    val published: String,
    val content: String,
    val likeCount: Int = 0,
    val repostCount: Int = 0,
    val viewCount: Int = 0,
    val likedByMe: Boolean = false,
)
