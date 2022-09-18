package com.bozin.jetpackcomposeblog.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.bozin.jetpackcomposeblog.presentation.add_post.AddPostScreen
import com.bozin.jetpackcomposeblog.presentation.auth.AuthScreen
import com.bozin.jetpackcomposeblog.presentation.auth.AuthViewModel
import com.bozin.jetpackcomposeblog.presentation.post_details.PostDetailsScreen
import com.bozin.jetpackcomposeblog.presentation.post_list.PostListScreen
import com.bozin.jetpackcomposeblog.presentation.theme.JetpackComposeBlogTheme
import com.bozin.jetpackcomposeblog.presentation.utils.Screens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeBlogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val authViewModel: AuthViewModel = hiltViewModel()
                    val navController = rememberNavController()

                    LaunchedEffect(key1 = true) {
                        authViewModel.eventFlow.collectLatest { event ->
                            when (event) {
                                is AuthViewModel.AuthEvent.LoggedIn -> {
                                    navController.navigate(Screens.PostListScreen.route) {
                                        popUpTo(route = Screens.AuthScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                                is AuthViewModel.AuthEvent.LoggedOut -> {
                                    navController.navigate(Screens.AuthScreen.route) {
                                        popUpTo(route = Screens.PostListScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = Screens.AuthScreen.route
                    ) {
                        composable(route = Screens.AuthScreen.route) {
                            AuthScreen(authViewModel = authViewModel)
                        }
                        composable(route = Screens.PostListScreen.route) {
                            PostListScreen(navController = navController, authViewModel = authViewModel)
                        }
                        composable(
                            route = Screens.PostDetailsScreen.route + "?postId={postId}",
                            arguments = listOf(
                                navArgument(
                                    name = "postId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            PostDetailsScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        composable(route = Screens.AddPostScreen.route) {
                            AddPostScreen(navController = navController, authViewModel = authViewModel)
                        }
                    }
                }
            }
        }
    }
}
