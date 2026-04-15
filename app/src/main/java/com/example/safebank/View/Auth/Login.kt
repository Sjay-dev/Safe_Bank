package com.example.safebank.View.Auth

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.safebank.R
import com.example.safebank.ViewModel.AuthUiState
import com.example.safebank.ViewModel.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.example.safebank.Navigation.LoginRoute
import com.example.safebank.Navigation.MainRoute
import com.example.safebank.Navigation.SignUpRoute

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Google Sign-In setup
    val signInClient: SignInClient = Identity.getSignInClient(context)
    val serverClientId = stringResource(R.string.default_web_client_id)

    var googleSignInInProgress by remember { mutableStateOf(false) }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val signInCredential = signInClient.getSignInCredentialFromIntent(result.data)
            val idToken = signInCredential.googleIdToken
            if (idToken != null) {
                viewModel.loginWithGoogle(idToken)
            } else {
                viewModel.uiState = AuthUiState.Error("No ID token received")
            }
        } catch (e: ApiException) {
            viewModel.uiState = AuthUiState.Error("Google sign-in failed: ${e.statusCode}")
        } catch (e: Exception) {
            viewModel.uiState = AuthUiState.Error(e.message ?: "Google sign-in failed")
        } finally {
            googleSignInInProgress = false
        }
    }

    fun launchGoogleSignIn() {
        googleSignInInProgress = true
        try {
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(serverClientId)
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .build()

            signInClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    try {
                        signInLauncher.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                                .build()
                        )
                    } catch (e: Exception) {
                        googleSignInInProgress = false
                        viewModel.uiState = AuthUiState.Error("Failed to launch sign-in: ${e.message}")
                    }
                }
                .addOnFailureListener { e ->
                    googleSignInInProgress = false
                    viewModel.uiState = AuthUiState.Error("Google sign-in not available: ${e.message}")
                }
        } catch (e: Exception) {
            googleSignInInProgress = false
            viewModel.uiState = AuthUiState.Error(e.message ?: "Google sign-in failed")
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            //Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            //  Show Error Message
            if (state is AuthUiState.Error) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            //Login Button
            Button(
                onClick = {
                viewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !googleSignInInProgress
            ) {
                if (state is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // OR Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(text = "  OR  ", style = MaterialTheme.typography.bodySmall)
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Login
            OutlinedButton(
                onClick = { launchGoogleSignIn() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                enabled = !googleSignInInProgress && state !is AuthUiState.Loading
            ) {
                if (googleSignInInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = "Google",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Login with Google")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Link
            Row {
                Text("Don't have an account yet? ")
                Text(
                    text = "Sign Up here",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        navController.navigate(SignUpRoute)
                    }
                )
            }
        }

        //  Navigate on success
        if (state is AuthUiState.LoginSuccess) {
            LaunchedEffect(state) {
                navController.navigate(
                    MainRoute(
                        name = state.name,
                        accountNumber = state.accountNumber,
                        balance = state.balance
                    )
                ) {
                    popUpTo<LoginRoute> { inclusive = true }
                }
            }
        }
    }
}