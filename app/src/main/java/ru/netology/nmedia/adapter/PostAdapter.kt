package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.formatCount

typealias OnLikeListener = (postId: Int) -> Unit
typealias OnRepostListener = (postId: Int) -> Unit

class PostAdapter(
    private val onLikeListener: OnLikeListener, private val onRepostListener: OnLikeListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onRepostListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onRepostListener: OnRepostListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = formatCount(post.likeCount)
            repostCount.text = formatCount(post.repostCount)
            viewCount.text = formatCount(post.viewCount)

            avatar.setImageResource(post.authorAvatar)
            likesButton.setImageResource(
                if (post.likedByMe) R.drawable.ic_heart_filled_24 else R.drawable.ic_heart_24
            )

            likesButton.setOnClickListener { onLikeListener(post.id) }
            repostButton.setOnClickListener { onRepostListener(post.id) }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldPost: Post, newPost: Post) = oldPost.id == newPost.id

    override fun areContentsTheSame(oldPost: Post, newPost: Post) = oldPost == newPost
}