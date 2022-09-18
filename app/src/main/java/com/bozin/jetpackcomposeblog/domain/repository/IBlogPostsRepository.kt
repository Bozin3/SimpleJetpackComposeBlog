package com.bozin.jetpackcomposeblog.domain.repository

import com.bozin.jetpackcomposeblog.data.remote.models.AddBlogPostDto
import com.bozin.jetpackcomposeblog.data.remote.models.AuthDto
import com.bozin.jetpackcomposeblog.data.remote.models.BlogPost
import com.bozin.jetpackcomposeblog.data.remote.models.TokenDto
import retrofit2.Response

interface IBlogPostsRepository {
    suspend fun getBlogPosts(): List<BlogPost>

    suspend fun getBlogById(id: Int): BlogPost

    suspend fun addBlogPost(addBlogPostDto: AddBlogPostDto): BlogPost

    suspend fun deleteBlogPost(id: Int): Response<Unit>

    suspend fun authenticate(authDto: AuthDto): TokenDto
}