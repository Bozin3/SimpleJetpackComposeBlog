package com.bozin.jetpackcomposeblog.presentation.add_post

import androidx.compose.ui.text.input.TextFieldValue
import com.bozin.jetpackcomposeblog.common.UiText

data class AddPostState (
    val isLoading: Boolean = false,
    val postContent: TextFieldValue = TextFieldValue(""),
    val error: UiText? = null
)