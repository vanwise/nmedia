package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
        val hasTextArg = arguments?.textArg != null

        with(binding) {
            val existedText = if (hasTextArg) {
                editGroup.visibility = View.VISIBLE
                arguments?.textArg
            } else {
                viewModel.draftText
            }

            AndroidUtils.showKeyboard(edit)
            existedText?.let { text ->
                if (text.isNotBlank()) {
                    editedMessage.text = text
                    edit.append(text)
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(!hasTextArg) {
                override fun handleOnBackPressed() {
                    val text = binding.edit.text.toString()
                    if (text.isNotBlank()) {
                        viewModel.draftText = text
                    }
                    remove()
                }
            })

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