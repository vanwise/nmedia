package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
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
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLikeById(postId: Int) = viewModel.likeById(postId)

            override fun onRepostById(postId: Int) = viewModel.repostById(postId)

            override fun onRemoveById(postId: Int) = viewModel.removeById(postId)

            override fun onEditById(post: Post) = viewModel.editById(post)
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { adapter.submitList(it) }

        binding.postSave.setOnClickListener {
            with(binding.postInput) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.content_is_blank_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.saveContent(text.toString())
                binding.editGroup.visibility = View.GONE
                clearTextAndFocusFromPostInput(this)
            }
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0) {
                with(binding.postInput) {
                    AndroidUtils.showKeyboard(this)
                    setText("")
                    append(post.content)
                }

                binding.editGroup.visibility = View.VISIBLE
                binding.editedMessage.text = post.content
            }
        }

        binding.cancelEdit.setOnClickListener {
            viewModel.resetEdited()
            binding.editGroup.visibility = View.GONE
            clearTextAndFocusFromPostInput(binding.postInput)
        }
    }

    private fun clearTextAndFocusFromPostInput(input: EditText) {
        with(input) {
            setText("")
            clearFocus()
            AndroidUtils.hideKeyboard(input)
        }
    }
}
