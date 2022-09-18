package com.bozin.jetpackcomposeblog.data.remote.models

data class ErrorResponse (
    val errors: List<String> = arrayListOf()
)