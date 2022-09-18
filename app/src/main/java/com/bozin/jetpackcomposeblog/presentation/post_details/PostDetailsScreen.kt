package com.bozin.jetpackcomposeblog.presentation.post_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.presentation.auth.AuthViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PostDetailsScreen(
    postDetailsViewModel: PostDetailsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val state = postDetailsViewModel.state.value

    LaunchedEffect(key1 = true) {
        postDetailsViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PostDetailsViewModel.UiEvent.PostDeleted -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {

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

        state.blogPost?.let { post ->
            Column(modifier = Modifier.fillMaxSize()) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                if (authViewModel.getCurrentLoggedUser() != null
                    && authViewModel.getCurrentLoggedUser()!!.id == state.blogPost.user.id) {
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp),
                        onClick = { postDetailsViewModel.deleteBlogPost() }) {
                        Text(text = stringResource(R.string.delete_post))
                    }
                }
            }
        }
    }
}