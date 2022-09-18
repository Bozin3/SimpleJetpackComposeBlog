package com.bozin.jetpackcomposeblog.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bozin.jetpackcomposeblog.common.Constants
import com.bozin.jetpackcomposeblog.common.SharedPrefsHandler
import com.bozin.jetpackcomposeblog.data.remote.BlogApi
import com.bozin.jetpackcomposeblog.data.remote.network_interceptors.AuthInterceptor
import com.bozin.jetpackcomposeblog.data.remote.repository.BlogPostsRepository
import com.bozin.jetpackcomposeblog.domain.repository.IBlogPostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(application: Application): SharedPreferences {
        return application.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPrefsHandler(sharedPreferences: SharedPreferences): SharedPrefsHandler {
        return SharedPrefsHandler(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPrefsHandler: SharedPrefsHandler): AuthInterceptor {
        return AuthInterceptor(sharedPrefsHandler)
    }

    @Provides
    @Singleton
    fun provideOkHttp(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideBlogApi(okHttpClient: OkHttpClient): BlogApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BLOG_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBlogRepository(api: BlogApi): IBlogPostsRepository {
        return BlogPostsRepository(api)
    }
}