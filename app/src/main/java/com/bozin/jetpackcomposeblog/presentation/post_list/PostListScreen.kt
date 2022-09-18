package com.bozin.jetpackcomposeblog.presentation.post_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.data.remote.models.BlogPost
import com.bozin.jetpackcomposeblog.presentation.auth.AuthViewModel
import com.bozin.jetpackcomposeblog.presentation.utils.Screens

@Composable
fun PostListScreen(
    postListViewModel: PostListViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val state = postListViewModel.state.value

    LaunchedEffect(Unit) {
        if (authViewModel.isAuthenticated()) {
            postListViewModel.getBlogPosts()
        } else {
            authViewModel.logOut()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.jetpack_compose_blog)) },
                actions = {

                    IconButton(onClick = {
                        if (authViewModel.isAuthenticated()) {
                            postListViewModel.getBlogPosts()
                        }
                    }) {
                        Icon(Icons.Default.Refresh, stringResource(R.string.refresh))
                    }

                    IconButton(onClick = {
                        authViewModel.logOut()
                    }) {
                        Icon(Icons.Default.ExitToApp, stringResource(R.string.log_out))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddPostScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_post))
            }
        },
        scaffoldState = scaffoldState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            if(state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if(state.error != null) {
                Text(
                    text = state.error.asString(),
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.blogPosts) { post ->
                    PostItem(
                        post = post,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        navController.navigate(Screens.PostDetailsScreen.route + "?postId=${it.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(post: BlogPost, modifier: Modifier, onItemClick: (BlogPost) -> Unit) {
    Card(modifier = modifier.clickable { onItemClick(post) }) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = rememberAsyncImagePainter(post.user.profileImageUrl),
                    contentDescription = stringResource(R.string.profile_pic),
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                        .clip(CircleShape)
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)) {
                    Text(
                        text = post.user.username,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = post.createdAt,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.body1,
                maxLines = 5
            )
        }
    }
}