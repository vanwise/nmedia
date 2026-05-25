package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater)
        val viewModel by activityViewModels<PostViewModel>()

        arguments?.textArg.let { text ->
            with(binding) {
                AndroidUtils.showKeyboard(edit)

                if (!text.isNullOrBlank()) {
                    editGroup.visibility = View.VISIBLE
                    editedMessage.text = text
                    edit.append(text)
                }
            }
        }

        binding.save.setOnClickListener {
            val text = binding.edit.text.toString()

            if (text.isNotBlank()) {
                viewModel.saveContent(text)
            }

            findNavController().navigateUp()
        }

        return binding.root
    }

    companion object {
        var Bundle.textArg by StringArg
    }
}