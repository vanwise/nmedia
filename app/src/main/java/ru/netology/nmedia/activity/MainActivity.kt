package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel by viewModels<PostViewModel>()

        val newPostLauncher = registerForActivityResult(NewPostResultContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.saveContent(result)
        }

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLikeById(postId: Int) = viewModel.likeById(postId)

            override fun onRepostById(postId: Int) = viewModel.repostById(postId)

            override fun onRemoveById(postId: Int) = viewModel.removeById(postId)

            override fun onEditById(post: Post) {
                viewModel.editById(post)
                newPostLauncher.launch(post.content)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { adapter.submitList(it) }

        binding.addButton.setOnClickListener {
            newPostLauncher.launch(null)
        }
    }
}
