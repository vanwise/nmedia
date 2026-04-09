package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val empty = Post()
    private val repository = PostRepositoryInMemoryImpl()
    val data = repository.get()

    val edited = MutableLiveData(empty)

    fun likeById(id: Int) = repository.likeById(id)

    fun repostById(id: Int) = repository.repostById(id)

    fun editById(post: Post) {
        edited.value = post
    }

    fun removeById(id: Int) = repository.removeById(id)

    fun saveContent(content: String) {
        edited.value?.let { post ->
            val trimmed = content.trim()
            if (post.content != trimmed) {
                repository.save(post.copy(content = trimmed))
            }
        }
        edited.value = empty
    }
}