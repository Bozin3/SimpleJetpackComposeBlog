package com.bozin.jetpackcomposeblog.presentation.add_post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bozin.jetpackcomposeblog.R
import com.bozin.jetpackcomposeblog.presentation.auth.AuthViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddPostScreen(
    addPostViewModel: AddPostViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val state = addPostViewModel.state.value

    LaunchedEffect(key1 = true) {
        addPostViewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddPostViewModel.UiEvent.PostAdded -> {
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

        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(5.dp),
                placeholder = { Text(text = stringResource(R.string.enter_message)) },
                value = state.postContent,
                onValueChange = { addPostViewModel.onContentValueChanged(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                textStyle = MaterialTheme.typography.body1
            )

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { addPostViewModel.addBlogPost(authViewModel.getCurrentLoggedUser()?.id ?: -1) }) {
                Text(text = stringResource(R.string.add_post))
            }
        }
    }
}