package com.bozin.jetpackcomposeblog.data.remote.repository

import com.bozin.jetpackcomposeblog.data.remote.BlogApi
import com.bozin.jetpackcomposeblog.data.remote.models.AddBlogPostDto
import com.bozin.jetpackcomposeblog.data.remote.models.AuthDto
import com.bozin.jetpackcomposeblog.data.remote.models.BlogPost
import com.bozin.jetpackcomposeblog.data.remote.models.TokenDto
import com.bozin.jetpackcomposeblog.domain.repository.IBlogPostsRepository
import retrofit2.Response
import javax.inject.Inject

class BlogPostsRepository @Inject constructor(
    private val api: BlogApi
): IBlogPostsRepository {

    override suspend fun getBlogPosts(): List<BlogPost> {
        return api.getBlogPosts()
    }

    override suspend fun getBlogById(id: Int): BlogPost {
        return api.getBlogPostById(id)
    }

    override suspend fun addBlogPost(addBlogPostDto: AddBlogPostDto): BlogPost {
        return api.addBlogPost(addBlogPostDto)
    }

    override suspend fun deleteBlogPost(id: Int): Response<Unit> {
        return api.deleteBlogPostById(id)
    }

    override suspend fun authenticate(authDto: AuthDto): TokenDto {
        return api.authenticate(authDto)
    }
}