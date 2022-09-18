package com.bozin.jetpackcomposeblog.presentation.post_details

import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.data.remote.models.BlogPost

data class PostDetailsState(
    val isLoading: Boolean = false,
    val blogPost: BlogPost? = null,
    val error: UiText? = null
)