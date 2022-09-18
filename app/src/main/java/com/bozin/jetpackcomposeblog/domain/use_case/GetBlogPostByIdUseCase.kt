package com.bozin.jetpackcomposeblog.domain.use_case

import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.common.Resource
import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.common.createAppropriateMessage
import com.bozin.jetpackcomposeblog.data.remote.models.BlogPost
import com.bozin.jetpackcomposeblog.domain.repository.IBlogPostsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBlogPostByIdUseCase @Inject constructor(
    private val repository: IBlogPostsRepository
) {
    operator fun invoke(id: Int): Flow<Resource<BlogPost>> = flow {
        try {
            emit(Resource.Loading())
            val blogPost = repository.getBlogById(id)
            emit(Resource.Success(blogPost))
        } catch(e: HttpException) {
            emit(Resource.Error(e.createAppropriateMessage()))
        } catch(e: IOException) {
            emit(Resource.Error(UiText.StringResource(R.string.network_exception)))
        }
    }
}