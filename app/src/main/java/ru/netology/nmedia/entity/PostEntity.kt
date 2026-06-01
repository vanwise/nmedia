package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String = "Me",
    val authorAvatar: Int = R.drawable.ic_netology_logo_48,
    val published: String = "Now",
    val content: String,
    val likeCount: Int = 0,
    val repostCount: Int = 0,
    val viewCount: Int = 0,
    val likedByMe: Boolean = false,
    val video: String? = ""
) {
    fun toPost() =
        Post(
            id = id,
            author = author,
            authorAvatar = authorAvatar,
            published = published,
            content = content,
            likeCount = likeCount,
            repostCount = repostCount,
            viewCount = viewCount,
            likedByMe = likedByMe,
            video = video,
        )

    companion object {
        fun fromPost(post: Post) =
            with(post) {
                PostEntity(
                    id = id,
                    author = author,
                    authorAvatar = authorAvatar,
                    published = published,
                    content = content,
                    likeCount = likeCount,
                    repostCount = repostCount,
                    viewCount = viewCount,
                    likedByMe = likedByMe,
                    video = video,
                )
            }
    }
}