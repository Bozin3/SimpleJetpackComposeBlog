package com.bozin.jetpackcomposeblog.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.common.Resource
import com.bozin.jetpackcomposeblog.common.SharedPrefsHandler
import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.data.remote.models.AuthDto
import com.bozin.jetpackcomposeblog.data.remote.models.User
import com.bozin.jetpackcomposeblog.domain.use_case.AuthenticateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase,
    private val sharedPrefsHandler: SharedPrefsHandler
) : ViewModel() {

    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    private val _eventFlow = MutableSharedFlow<AuthEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            sharedPrefsHandler.getAuthData { token, loggedUser ->
                _state.value = AuthState(token = token, loggedUsed = loggedUser)
                if (isAuthenticated()) {
                    moveToMainScreen()
                }
            }
        }
    }

    fun isAuthenticated(): Boolean {
        return !_state.value.token.isNullOrEmpty() && _state.value.loggedUsed != null
    }

    fun authenticate(username: String, password: String) {

        if (username.isBlank() || password.isBlank()) {
            viewModelScope.launch {
                _eventFlow.emit(AuthEvent.Exception(UiText.StringResource(R.string.please_fill_all_fields)))
            }
            return
        }

        authenticateUseCase(AuthDto(username, password)).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    sharedPrefsHandler.saveAuthData(result.data?.token, result.data?.user)
                    _state.value = AuthState(token = result.data?.token, loggedUsed = result.data?.user)
                    if (isAuthenticated()) {
                        moveToMainScreen()
                    }
                }
                is Resource.Error -> {
                    _state.value = AuthState()
                    val uiMessage = result.message ?: UiText.StringResource(R.string.unexpected_error_occured)
                    _eventFlow.emit(AuthEvent.Exception(uiMessage))
                }
                is Resource.Loading -> {
                    _state.value = AuthState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun moveToMainScreen() {
        viewModelScope.launch {
            _eventFlow.emit(AuthEvent.LoggedIn)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            sharedPrefsHandler.clearAuthData()
            _state.value = AuthState()
            _eventFlow.emit(AuthEvent.LoggedOut)
        }
    }

    fun getCurrentLoggedUser(): User? {
        return _state.value.loggedUsed
    }

    sealed class AuthEvent(val message: UiText = UiText.DynamicString("")) {
        object LoggedIn: AuthEvent()
        object LoggedOut: AuthEvent()
        class Exception(message: UiText): AuthEvent(message)
    }
}