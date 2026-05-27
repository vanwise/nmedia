package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.get()
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id == id) it.copy(
                likedByMe = !it.likedByMe,
                likeCount = if (it.likedByMe) it.likeCount - 1 else it.likeCount + 1
            ) else it
        }
        data.value = posts
    }

    override fun repostById(id: Int) {
        dao.repostById(id)
        posts = posts.map {
            if (it.id == id) it.copy(repostCount = it.repostCount + 1) else it
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val postId = post.id
        val saved = dao.save(post)

        posts = if (postId == 0) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id == postId) saved else it
            }
        }

        data.value = posts
    }
}