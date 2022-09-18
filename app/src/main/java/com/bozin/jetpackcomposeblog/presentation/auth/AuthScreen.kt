package com.bozin.jetpackcomposeblog.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bozin.jetpackcomposeblog.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel
) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val state = authViewModel.state.value
    var usernameField by remember { mutableStateOf("") }
    var passwordField by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (authViewModel.isAuthenticated()) {
            authViewModel.moveToMainScreen()
        }
    }

    LaunchedEffect(key1 = true) {
        authViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AuthViewModel.AuthEvent.Exception -> {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {

            Column(modifier = Modifier.align(Alignment.Center)) {

                if(state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 20.dp))
                }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(5.dp),
                    placeholder = { Text(text = stringResource(R.string.enter_username)) },
                    value = usernameField,
                    onValueChange = { usernameField = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    textStyle = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.padding(top = 10.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(5.dp),
                    placeholder = { Text(text = stringResource(R.string.enter_password)) },
                    value = passwordField,
                    onValueChange = { passwordField = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.padding(top = 10.dp))

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { authViewModel.authenticate(usernameField, passwordField) }) {
                    Text(text = stringResource(R.string.log_in))
                }
            }
        }
    }
}