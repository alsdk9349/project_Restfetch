package com.suisei.restfetch.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.suisei.restfetch.presentation.intent.AccountIntent
import com.suisei.restfetch.presentation.intent.VerifyEmailIntent
import com.suisei.restfetch.presentation.state.AccountViewState
import com.suisei.restfetch.presentation.state.VerifyEmailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow<AccountViewState>(AccountViewState.Login)
    val viewState: StateFlow<AccountViewState> get() = _viewState
    private val accountIntent = Channel<AccountIntent>()

    private val _verifyEmailState = MutableStateFlow<VerifyEmailState>(VerifyEmailState.WaitRequestCode)
    val verifyEmailState: StateFlow<VerifyEmailState> get() = _verifyEmailState
    private val verifyEmailIntent = Channel<VerifyEmailIntent>()

    init {
        handleViewIntent()
        handleVerifyEmailIntent()
    }

    fun sendViewIntent(intent: AccountIntent) = viewModelScope.launch(Dispatchers.IO) {
        accountIntent.send(intent)
    }

    fun sendVerifyEmailIntent(intent: VerifyEmailIntent) = viewModelScope.launch(Dispatchers.IO) {
        verifyEmailIntent.send(intent)
    }

    private fun handleViewIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            accountIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    AccountIntent.LoadLogin -> loadLogin()
                    AccountIntent.LoadSignUp -> loadSignUp()
                    AccountIntent.LoadForgotPassword -> loadForgotPassword()
                }
            }
        }
    }

    private fun loadLogin() {
        _viewState.value = AccountViewState.Login
    }

    private fun loadSignUp() {
        _viewState.value = AccountViewState.SignUp
    }

    private fun loadForgotPassword() {
        _viewState.value = AccountViewState.ForgotPassword
    }

    private fun handleVerifyEmailIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            verifyEmailIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    VerifyEmailIntent.LoadRequestResendButton -> loadRequestResendButton()
                    VerifyEmailIntent.LoadRequestVerifyButton -> loadRequestVerifyButton()
                    VerifyEmailIntent.LoadVerifyComplete -> loadCompleteText()
                }
            }
        }
    }

    private fun loadRequestResendButton() {
        _verifyEmailState.value = VerifyEmailState.WaitEnterCode
    }

    private fun loadRequestVerifyButton() {
        _verifyEmailState.value = VerifyEmailState.WaitRequestVerify
    }

    private fun loadCompleteText() {
        _verifyEmailState.value = VerifyEmailState.VerifyComplete
    }

    fun requestGoogleAuth(context: Context) {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("296076045402-87ufif57u67kshu0q58bv71fai8bolpu.apps.googleusercontent.com")
            .setAutoSelectEnabled(false)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                e.message?.let { Log.e("TEST", it) }
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential
        Log.e("TEST", credential.type)
        when (credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                val responseJson = credential.authenticationResponseJson
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        Log.e("TEST", googleIdTokenCredential.id)
                        googleIdTokenCredential.displayName?.let { Log.e("TEST", it) }
                        // You can use the members of googleIdTokenCredential directly for UX
                        // purposes, but don't use them to store or control access to user
                        // data. For that you first need to validate the token:
                        // pass googleIdTokenCredential.getIdToken() to the backend server.
                        /*GoogleIdTokenVerifier verifier = ... // see validation instructions
                        GoogleIdToken idToken = verifier.verify(idTokenString);
                        // To get a stable account identifier (e.g. for storing user data),
                        // use the subject ID:
                        idToken.getPayload().getSubject()*/
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("TEST", "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e("TEST", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("TEST", "Unexpected type of credential")
            }
        }
    }
}