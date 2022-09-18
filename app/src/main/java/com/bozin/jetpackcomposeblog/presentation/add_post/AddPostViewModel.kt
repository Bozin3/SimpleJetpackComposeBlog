package com.bozin.jetpackcomposeblog.presentation.add_post

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.common.Resource
import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.data.remote.models.AddBlogPostDto
import com.bozin.jetpackcomposeblog.domain.use_case.AddBlogPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val addBlogPostUseCase: AddBlogPostUseCase
) : ViewModel() {

    private val _state = mutableStateOf(AddPostState())
    val state: State<AddPostState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onContentValueChanged(value: TextFieldValue) {
        _state.value = AddPostState(
            postContent = value
        )
    }

    fun addBlogPost(userId: Int) {

        val content = _state.value.postContent.text

        if (content.isEmpty()) {
            _state.value = AddPostState(
                error = UiText.StringResource(R.string.please_enter_some_content)
            )
            return
        }

        val addBlogPostDto = AddBlogPostDto(content, userId)
        addBlogPostUseCase(addBlogPostDto).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UiEvent.PostAdded)
                }
                is Resource.Error -> {
                    _state.value = AddPostState(
                        error = result.message ?: UiText.StringResource(R.string.unexpected_error_occured)
                    )
                }
                is Resource.Loading -> {
                    _state.value = AddPostState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        object PostAdded: UiEvent()
    }
}