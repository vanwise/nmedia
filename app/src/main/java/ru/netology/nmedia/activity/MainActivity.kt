package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

        val viewModel by viewModels<PostViewModel>()

        viewModel.data.observe(this) { post ->
            with(binding) {
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
            }
        }

        with(binding) {
            likesButton.setOnClickListener { viewModel.like() }
            repostButton.setOnClickListener { viewModel.repost() }
        }
    }

    private fun formatCount(count: Int): String {
        val oneDecimalFormatter = DecimalFormat("#.#").apply {
            roundingMode = RoundingMode.DOWN
        }

        return when {
            count >= 1_000_000 -> "${oneDecimalFormatter.format(count.toDouble() / 1_000_000)}M"
            count > 10_000 -> "${count / 1_000}K"
            count >= 1_000 -> "${oneDecimalFormatter.format(count.toDouble() / 1_000)}K"
            else -> count.toString()
        }
    }
}