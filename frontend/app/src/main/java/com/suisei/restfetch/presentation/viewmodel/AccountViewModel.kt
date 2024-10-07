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
import com.google.gson.JsonParser
import com.suisei.restfetch.data.remote.ServerClient
import com.suisei.restfetch.data.remote.UserAPI
import com.suisei.restfetch.data.repository.AccountRepository
import com.suisei.restfetch.data.repository.MyDataRepository
import com.suisei.restfetch.data.repository.NotifyRepository
import com.suisei.restfetch.presentation.intent.AccountIntent
import com.suisei.restfetch.presentation.intent.VerifyEmailIntent
import com.suisei.restfetch.presentation.state.AccountViewState
import com.suisei.restfetch.presentation.state.VerifyEmailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val notifyRepository: NotifyRepository,
    private val myDataRepository: MyDataRepository
) : ViewModel() {
    private val accountIntent = Channel<AccountIntent>()
    private val verifyIntent = Channel<VerifyEmailIntent>()

    private val retrofit = ServerClient.userRetrofit

    val viewState = repository.viewState
    val verifyState = repository.verifyState

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
        repository.updateViewState(AccountViewState.Login)
    }

    private fun loadSignUp() {
        repository.updateViewState(AccountViewState.SignUp)
        repository.updateVerifyState(VerifyEmailState.WaitRequest)

    }

    private fun loadForgotPassword() {
        repository.updateViewState(AccountViewState.ForgotPassword)
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
        repository.updateVerifyState(VerifyEmailState.WaitCode)
    }

    private fun loadVerifyButton() {
        repository.updateVerifyState(VerifyEmailState.WaitVerification)
    }

    private fun loadCompleteText() {
        repository.updateVerifyState(VerifyEmailState.VerifyComplete)
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
                        } else {
                            notifyRepository.showNotify(responseBody.message)
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

                        if (responseBody!!.code == UserAPI.VerifySuccess) {
                            sendVerifyIntent(VerifyEmailIntent.LoadCompleteText)
                        } else {
                            notifyRepository.showNotify(responseBody.message)
                        }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("TEST", it) }
            }

        }
    }

    fun createAccount(email: String, password: String, nickname: String) {
        val body = mapOf("email" to email, "password" to password, "nickname" to nickname)

        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.signup(body)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody!!.code == UserAPI.CreateSuccess) {
                        sendViewIntent(AccountIntent.LoadLogin)
                    } else {
                        notifyRepository.showNotify(responseBody.message)
                    }
                }
            }
        }
    }

    private fun saveLoginInfo(context: Context, email: String, password: String) {
        val sharedPreferences =
            context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    fun login(context: Context, email: String, password: String, onSuccess: () -> Unit) {
        val body = mapOf("email" to email, "password" to password)
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.login(body)
            Log.e("TEST", response.raw().toString())
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    val responseBody = response.body()

                    if(responseBody!!.code == UserAPI.LoginSuccess) {
                        myDataRepository.setUser(responseBody.data)
                        saveLoginInfo(context, email, password)

                        onSuccess()
                    }
                } else {
                    handleResponseError(response.errorBody())
                }
            }
        }
    }

    private fun handleResponseError(errorResponseBody: ResponseBody?) {
        Log.e("TEST", errorResponseBody!!.string())
        val error = JsonParser.parseString(errorResponseBody.string()).asJsonObject
        Log.e("TEST", "handleResponseError")
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