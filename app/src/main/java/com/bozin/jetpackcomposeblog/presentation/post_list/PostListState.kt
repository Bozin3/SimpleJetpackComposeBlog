package com.bozin.jetpackcomposeblog.presentation.post_list

import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.data.remote.models.BlogPost

data class PostListState(
    val isLoading: Boolean = false,
    val blogPosts: List<BlogPost> = emptyList(),
    val error: UiText? = null
)