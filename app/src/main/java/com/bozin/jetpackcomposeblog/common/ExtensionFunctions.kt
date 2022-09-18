package com.bozin.jetpackcomposeblog.common

import com.bozin.jetpackcomposeblog.R
import retrofit2.HttpException

fun HttpException.createAppropriateMessage(): UiText {
    return when (this.code()) {
        400 -> {
            UiText.StringResource(R.string.invalid_input_data_please_try_again)
        }
        401 -> {
            UiText.StringResource(R.string.unauthorized_make_sure_you_are_logged_in)
        }
        else -> {
            if (this.localizedMessage != null) UiText.DynamicString(this.localizedMessage) else UiText.StringResource(R.string.unexpected_error_occured)
        }
    }
}