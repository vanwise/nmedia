package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun get(): List<Post>
    fun likeById(id: Int)
    fun repostById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post): Post
}