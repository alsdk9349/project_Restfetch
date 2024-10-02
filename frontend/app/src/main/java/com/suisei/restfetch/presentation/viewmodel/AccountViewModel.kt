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
import com.suisei.restfetch.data.remote.ServerClient
import com.suisei.restfetch.data.remote.UserAPI
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow<AccountViewState>(AccountViewState.Login)
    val viewState: StateFlow<AccountViewState> get() = _viewState
    private val accountIntent = Channel<AccountIntent>()

    private val _verifyState = MutableStateFlow<VerifyEmailState>(VerifyEmailState.WaitRequest)
    val verifyState: StateFlow<VerifyEmailState> get() = _verifyState
    private val verifyIntent = Channel<VerifyEmailIntent>()

    private val retrofit = ServerClient.userRetrofit

    init {
        handleViewIntent()
        handleVerifyIntent()
    }

    fun sendViewIntent(intent: AccountIntent) = viewModelScope.launch(Dispatchers.IO) {
        accountIntent.send(intent)
    }

    fun sendVerifyIntent(intent: VerifyEmailIntent) = viewModelScope.launch(Dispatchers.IO) {
        verifyIntent.send(intent)
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
        _verifyState.value = VerifyEmailState.WaitRequest
    }

    private fun loadForgotPassword() {
        _viewState.value = AccountViewState.ForgotPassword
    }

    private fun handleVerifyIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            verifyIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    VerifyEmailIntent.LoadResendButton -> loadResendButton()
                    VerifyEmailIntent.LoadVerifyButton -> loadVerifyButton()
                    VerifyEmailIntent.LoadCompleteText -> loadCompleteText()
                }
            }
        }
    }

    private fun loadResendButton() {
        _verifyState.value = VerifyEmailState.WaitCode
    }

    private fun loadVerifyButton() {
        _verifyState.value = VerifyEmailState.WaitVerification
    }

    private fun loadCompleteText() {
        _verifyState.value = VerifyEmailState.VerifyComplete
    }

    fun requestCode(email: String) {

        val body = mapOf("email" to email)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofit.requestCode(body)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody!!.code == UserAPI.SendSuccess) {
                            sendVerifyIntent(VerifyEmailIntent.LoadResendButton)
                        }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("TEST", it) }
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        val body = mapOf("email" to email, "verificationCode" to code)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofit.verifyCode(body)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if(responseBody!!.code == UserAPI.VerifySuccess) {
                            sendVerifyIntent(VerifyEmailIntent.LoadCompleteText)
                        }
                    }
                }
            } catch(e: Exception) {
                e.message?.let { Log.e("TEST", it) }
            }

        }
    }

    fun createAccount(email: String, password: String, nickname: String) {
        val body = mapOf("email" to email, "password" to password, "nickname" to nickname)

        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.signup(body)

            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    val responseBody = response.body()

                    if(responseBody!!.code == UserAPI.CreateSuccess) {
                        sendViewIntent(AccountIntent.LoadLogin)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String, onSuccess: (accessToken: String, refreshToken: String) -> Unit) {
        val body = mapOf("email" to email, "password" to password)
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.login(body)
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    val responseHeaders = response.headers()
                    val responseBody = response.body()
                    val values = responseHeaders.values("Set-Cookie")

                    val keys = responseBody!!.toString()
                    Log.e("TEST", keys)


                    val userData = response.body()!!.data

                    val accessToken = extractToken("accessToken", values[0])
                    val refreshToken = extractToken("refreshToken", values[1])

                    if(responseBody!!.code == UserAPI.LoginSuccess) {
                        onSuccess(accessToken, refreshToken)
                    }
                }
            }
        }
    }

    private fun extractToken(tokenName: String, cookieString: String): String {
        val regex = Regex("$tokenName=([^;]+)")
        val accessKey = regex.find(cookieString)
        return accessKey?.groups?.get(1)?.value!!
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