package com.bozin.jetpackcomposeblog.data.remote.network_interceptors

import com.bozin.jetpackcomposeblog.common.SharedPrefsHandler
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPrefsHandler: SharedPrefsHandler
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            val token = sharedPrefsHandler.getToken() ?: return chain.proceed(request)
            chain.proceed(
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            )
        } catch (e: Exception) {
            chain.proceed(request)
        }
    }
}