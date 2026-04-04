package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun repostById(id: Int)
}