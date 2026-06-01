package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {
    override fun get(): LiveData<List<Post>> = dao.get().map { posts ->
        posts.map { it.toPost() }
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
    }

    override fun repostById(id: Int) {
        dao.repostById(id)
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromPost(post))
    }
}