package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(private val context: Context) : PostRepository {
    private var posts = readPosts()
        set(value) {
            field = value
            sync()
        }
    private var nextId = (posts.maxByOrNull { it.id }?.id ?: 0) + 1
    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        posts = posts.map {
            if (it.id == id) it.copy(
                likedByMe = !it.likedByMe,
                likeCount = if (it.likedByMe) it.likeCount - 1 else it.likeCount + 1,
            ) else it
        }
        data.value = posts
    }

    override fun repostById(id: Int) {
        posts = posts.map {
            if (it.id == id) it.copy(repostCount = it.repostCount + 1) else it
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0) {
            listOf(post.copy(id = nextId++, author = "Me", published = "Now")) + posts
        } else {
            posts.map {
                if (it.id == post.id) it.copy(content = post.content) else it
            }
        }

        data.value = posts
    }

    private fun readPosts(): List<Post> {
        val file = context.filesDir.resolve(FILE_NAME)
        return if (file.exists()) {
            file.reader().buffered().use {
                gson.fromJson(it, postsType)
            }
        } else {
            emptyList()
        }
    }

    private fun sync() {
        val file = context.filesDir.resolve(FILE_NAME)
        file.writer().buffered().use {
            it.write(gson.toJson(posts))
        }
    }

    companion object {
        private const val FILE_NAME = "posts.json"
        private val gson = Gson()
        private val postsType = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}