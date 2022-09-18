package com.bozin.jetpackcomposeblog.data.remote.models

data class BlogPost(
    val content: String,
    val createdAt: String,
    val id: Int,
    val user: User
)