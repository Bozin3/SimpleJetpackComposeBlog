package com.bozin.jetpackcomposeblog.data.remote

import com.bozin.jetpackcomposeblog.data.remote.models.AddBlogPostDto
import com.bozin.jetpackcomposeblog.data.remote.models.AuthDto
import com.bozin.jetpackcomposeblog.data.remote.models.BlogPost
import com.bozin.jetpackcomposeblog.data.remote.models.TokenDto
import retrofit2.http.*

interface BlogApi {

    @GET("/api/posts")
    suspend fun getBlogPosts(): List<BlogPost>

    @GET("/api/posts/{id}")
    suspend fun getBlogPostById(@Path("id") id: Int): BlogPost

    @POST("/api/posts")
    suspend fun addBlogPost(@Body addBlogPostDto: AddBlogPostDto): BlogPost

    @DELETE("/api/posts/{id}")
    suspend fun deleteBlogPostById(@Path("id") id: Int): retrofit2.Response<Unit>

    @POST("/api/auth")
    suspend fun authenticate(@Body authDto: AuthDto): TokenDto
}