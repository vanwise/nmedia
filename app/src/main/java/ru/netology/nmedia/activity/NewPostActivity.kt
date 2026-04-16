package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.utils.AndroidUtils

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        intent.let {
            val text = it.getStringExtra(Intent.EXTRA_TEXT)

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

            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val intent = Intent().putExtra(Intent.EXTRA_TEXT, text)
                setResult(RESULT_OK, intent)
            }

            finish()
        }
    }
}