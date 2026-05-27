package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE} (
                ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
                ${PostColumns.COLUMN_AUTHOR_AVATAR} INTEGER,
                ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL, 
                ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
                ${PostColumns.COLUMN_LIKE_COUNT} INTEGER NOT NULL DEFAULT 0, 
                ${PostColumns.COLUMN_REPOST_COUNT} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_VIEW_COUNT} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_VIDEO} TEXT
            );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_AUTHOR_AVATAR = "authorAvatar"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_LIKE_COUNT = "likeCount"
        const val COLUMN_REPOST_COUNT = "repostCount"
        const val COLUMN_VIEW_COUNT = "viewCount"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_AUTHOR_AVATAR,
            COLUMN_PUBLISHED,
            COLUMN_CONTENT,
            COLUMN_LIKE_COUNT,
            COLUMN_REPOST_COUNT,
            COLUMN_VIEW_COUNT,
            COLUMN_LIKED_BY_ME,
            COLUMN_VIDEO,
        )
    }

    override fun removeById(id: Int) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun save(post: Post): Post {
        val postId = post.id
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "Now")
        }

        val id = if (postId == 0) {
            db.insert(PostColumns.TABLE, null, values)
        } else {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(postId.toString())
            )
            postId
        }

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun repostById(id: Int) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE} SET
                    ${PostColumns.COLUMN_REPOST_COUNT} = ${PostColumns.COLUMN_REPOST_COUNT} + 1
                WHERE ${PostColumns.COLUMN_ID} = ?
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun likeById(id: Int) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE} SET
                    ${PostColumns.COLUMN_LIKE_COUNT} = ${PostColumns.COLUMN_LIKE_COUNT} + CASE 
                        WHEN ${PostColumns.COLUMN_LIKED_BY_ME} THEN -1 ELSE 1 END,
                    ${PostColumns.COLUMN_LIKED_BY_ME} = CASE WHEN ${PostColumns.COLUMN_LIKED_BY_ME}
                        THEN 0 ELSE 1 END
                WHERE ${PostColumns.COLUMN_ID} = ?
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun get(): List<Post> {
        val posts = mutableListOf<Post>()

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }

        return posts
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                authorAvatar = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR_AVATAR)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                likeCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKE_COUNT)),
                repostCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_REPOST_COUNT)),
                viewCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEW_COUNT)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                video = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO)),
            )
        }
    }
}