package com.bozin.jetpackcomposeblog.presentation.auth

import com.bozin.jetpackcomposeblog.data.remote.models.User

data class AuthState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val loggedUsed: User? = null
)