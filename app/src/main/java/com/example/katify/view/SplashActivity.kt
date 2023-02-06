package com.example.katify.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.katify.R
import com.example.katify.data.model.User
import com.example.katify.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

/**
 *
 * This activity is the entrypoint of the app, and its responsible for
 * searching if the user has already signed in the app
 *
 * Inherits [AppCompatActivity]
 *
 */
class SplashActivity : AppCompatActivity() {
    private lateinit var loginVM: LoginViewModel

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        loginVM = ViewModelProvider(this).get(LoginViewModel::class.java)

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener {
            checkIfUserHasAlreadyLogged()
            false
        }
        checkIfUserHasAlreadyLogged()
    }

    /**
     * Check if the user has already signed, if so gets credentials,
     * else call [goToLoginActivity]
     */
    private fun checkIfUserHasAlreadyLogged() {
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null && !account.isExpired) {
            getGoogleAuthCredential(account)
        } else {
            goToLoginActivity()
        }
    }

    /**
     * Starts [LoginActivity] activity
     */
    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     * Get [googleSignInAccount] credentials
     */
    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    /**
     * Gets authenticated user and calls [goToKanbansActivity]
     */
    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        loginVM.signInWithGoogle(googleAuthCredential)
        loginVM.authenticatedUserLiveData.observe(this) { authenticatedUser ->
            goToKanbansActivity(authenticatedUser)
        }
    }

    /**
     * Starts [KanbansPageActivity] activity, passing [user] to it
     */
    private fun goToKanbansActivity(user: User) {
        val intent = Intent(this, KanbansPageActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }


}
