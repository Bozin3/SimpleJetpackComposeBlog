package com.bozin.jetpackcomposeblog.presentation.post_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.common.Resource
import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.domain.use_case.GetBlogPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val getBlogPostsUseCase: GetBlogPostsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(PostListState())
    val state: State<PostListState> = _state

    fun getBlogPosts() {
        getBlogPostsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = PostListState(blogPosts = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = PostListState(
                        error =  result.message ?: UiText.StringResource(R.string.unexpected_error_occured)
                    )
                }
                is Resource.Loading -> {
                    _state.value = PostListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}