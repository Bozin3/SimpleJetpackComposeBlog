package com.bozin.jetpackcomposeblog.presentation.post_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.common.Resource
import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.domain.use_case.DeleteBlogPostByIdUseCase
import com.bozin.jetpackcomposeblog.domain.use_case.GetBlogPostByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val getBlogPostByIdUseCase: GetBlogPostByIdUseCase,
    private val deleteBlogPostByIdUseCase: DeleteBlogPostByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(PostDetailsState())
    val state: State<PostDetailsState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var blogPostId = -1

    init {
        savedStateHandle.get<Int>("postId")?.let { postId ->
            blogPostId = postId
            if(blogPostId != -1) {
                getBlogPostById(blogPostId)
            }
        }
    }

    fun getBlogPostById(id: Int) {
        getBlogPostByIdUseCase(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = PostDetailsState(blogPost = result.data)
                }
                is Resource.Error -> {
                    _state.value = PostDetailsState(
                        error = result.message ?: UiText.StringResource(R.string.unexpected_error_occured)
                    )
                }
                is Resource.Loading -> {
                    _state.value = PostDetailsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteBlogPost() {
        deleteBlogPostByIdUseCase(blogPostId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UiEvent.PostDeleted)
                }
                is Resource.Error -> {
                    _state.value = PostDetailsState(
                        error = result.message ?: UiText.StringResource(R.string.unexpected_error_occured)
                    )
                }
                is Resource.Loading -> {
                    _state.value = PostDetailsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        object PostDeleted: UiEvent()
    }
}