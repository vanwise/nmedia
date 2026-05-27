package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val empty = Post()
    private val repository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )
    val data = repository.get()

    val edited = MutableLiveData(empty)
    var draftText: String? = null

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
                if (post.id == 0) {
                    draftText = null
                }
            }
        }
        resetEdited()
    }

    fun resetEdited() {
        edited.value = empty
    }
}