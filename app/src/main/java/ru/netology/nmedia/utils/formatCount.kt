package ru.netology.nmedia.utils

import java.math.RoundingMode
import java.text.DecimalFormat

fun formatCount(count: Int): String {
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