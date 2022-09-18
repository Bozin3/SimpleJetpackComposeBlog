package com.bozin.jetpackcomposeblog.common

import android.content.SharedPreferences
import com.bozin.jetpackcomposeblog.data.remote.models.User
import com.google.gson.Gson

class SharedPrefsHandler(private val sharedPreferences: SharedPreferences) {

    private val gson: Gson = Gson()

    fun saveAuthData(token: String?, user: User?) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SHARED_PREFS_TOKEN_KEY, token)
        editor.putString(Constants.SHARED_PREFS_USER_KEY, gson.toJson(user))
        editor.apply()
    }

    fun getAuthData(onComplete: (String?, User?) -> Unit) {
        val token = sharedPreferences.getString(Constants.SHARED_PREFS_TOKEN_KEY, "")
        val userJsonString = sharedPreferences.getString(Constants.SHARED_PREFS_USER_KEY, "")
        val user = gson.fromJson(userJsonString, User::class.java)
        onComplete(token, user)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(Constants.SHARED_PREFS_TOKEN_KEY, "")
    }

    fun clearAuthData() {
        sharedPreferences.edit().clear().apply()
    }
}