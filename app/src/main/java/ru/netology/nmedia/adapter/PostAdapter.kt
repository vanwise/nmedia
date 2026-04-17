package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.formatCount

interface OnInteractionListener {
    fun onLikeById(postId: Int)
    fun onRepostById(postId: Int)
    fun onRemoveById(postId: Int)
    fun onEditById(post: Post)
    fun onClickVideoPreview(video: String)
}

class PostAdapter(
    private val listener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding, private val listener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            repostButton.text = formatCount(post.repostCount)
            viewCount.text = formatCount(post.viewCount)

            videoPreviewGroup.visibility =
                if (post.video.isNullOrBlank()) View.GONE else View.VISIBLE
            post.video?.let { video ->
                videoPreview.setOnClickListener { listener.onClickVideoPreview(video) }
            }

            likesButton.isChecked = post.likedByMe
            likesButton.text = formatCount(post.likeCount)

            avatar.setImageResource(post.authorAvatar)

            likesButton.setOnClickListener { listener.onLikeById(post.id) }
            repostButton.setOnClickListener { listener.onRepostById(post.id) }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menuEditPost -> {
                                listener.onEditById(post)
                                true
                            }

                            R.id.menuRemovePost -> {
                                listener.onRemoveById(post.id)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldPost: Post, newPost: Post) = oldPost.id == newPost.id

    override fun areContentsTheSame(oldPost: Post, newPost: Post) = oldPost == newPost
}