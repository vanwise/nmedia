package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository = PostRepositoryInMemoryImpl()
    val data = repository.get()

    fun likeById(id: Int) = repository.likeById(id)
    fun repostById(id: Int) = repository.repostById(id)
}