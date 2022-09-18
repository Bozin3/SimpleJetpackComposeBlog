package com.bozin.jetpackcomposeblog.presentation.utils

sealed class Screens(val route: String) {
    object PostListScreen: Screens("post_list_screen")
    object PostDetailsScreen: Screens("post_details_screen")
    object AddPostScreen: Screens("add_post_screen")
    object AuthScreen: Screens("auth_screen")
}