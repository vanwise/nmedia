package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragment.NewPostFragment.Companion.textArg
import ru.netology.nmedia.fragment.PostFragment.Companion.postIdArg
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater)
        val viewModel by activityViewModels<PostViewModel>()

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLikeById(postId: Int) = viewModel.likeById(postId)

            override fun onRepostById(postId: Int) = viewModel.repostById(postId)

            override fun onRemoveById(postId: Int) = viewModel.removeById(postId)

            override fun onEditById(post: Post) {
                viewModel.editById(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply { textArg = post.content })
            }

            override fun onClick(postId: Int) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    Bundle().apply { postIdArg = postId })
            }

            override fun onClickVideoPreview(video: String) {
                val intent = Intent(Intent.ACTION_VIEW, video.toUri())
                val intentWithChooser = Intent.createChooser(
                    intent, getString(R.string.choose_app_for_play_video)
                )
                startActivity(intentWithChooser)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}