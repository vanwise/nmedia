package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * from posts")
    fun get(): LiveData<List<PostEntity>>

    @Query(
        """
        UPDATE posts SET
            likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
    """
    )
    fun likeById(id: Int)

    @Query(
        """
        UPDATE posts SET
            repostCount = repostCount + 1
        WHERE id = :id
    """
    )
    fun repostById(id: Int)

    @Query("DELETE from posts WHERE id = :id")
    fun removeById(id: Int)

    @Upsert
    fun save(post: PostEntity)
}