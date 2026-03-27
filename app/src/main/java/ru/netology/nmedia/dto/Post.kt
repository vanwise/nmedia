package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val authorAvatar: Int,
    val published: String,
    val content: String,
    var likeCount: Int = 0,
    var repostCount: Int = 0,
    var viewCount: Int = 0,
    var likedByMe: Boolean = false,
)
