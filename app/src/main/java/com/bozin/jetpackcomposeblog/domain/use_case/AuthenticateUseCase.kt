package com.bozin.jetpackcomposeblog.domain.use_case

import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.common.Resource
import com.bozin.jetpackcomposeblog.common.UiText
import com.bozin.jetpackcomposeblog.data.remote.models.AuthDto
import com.bozin.jetpackcomposeblog.data.remote.models.TokenDto
import com.bozin.jetpackcomposeblog.domain.repository.IBlogPostsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthenticateUseCase @Inject constructor(
    private val repository: IBlogPostsRepository
) {
    operator fun invoke(authDto: AuthDto): Flow<Resource<TokenDto>> = flow {
        try {
            emit(Resource.Loading())
            val tokenDto = repository.authenticate(authDto)
            emit(Resource.Success(tokenDto))
        } catch(e: HttpException) {
            when (e.code()) {
                401 -> {
                    emit(Resource.Error(UiText.StringResource(R.string.wrong_username_password)))
                }
                else -> {
                    val uiMessage = if (e.localizedMessage != null) UiText.DynamicString(e.localizedMessage) else UiText.StringResource(R.string.unexpected_error_occured)
                    emit(Resource.Error(uiMessage))
                }
            }
        } catch(e: IOException) {
            emit(Resource.Error(UiText.StringResource(R.string.network_exception)))
        }
    }
}