package com.example.katify.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.katify.R
import com.example.katify.data.User
import com.example.katify.databinding.ActivityLoginBinding
import com.example.katify.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginVM: LoginViewModel

    lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val gso: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleLoginBtn.setOnClickListener(this)

        loginVM = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {
            getGoogleAuthCredential(account)
        }
    }

    override fun onClick(v: View?) {
        println(v)
        if (v!!.id == R.id.google_login_btn) {
            openSomeActivityForResult()
        }
    }

    fun openSomeActivityForResult() {
        val signInIntent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if (account != null) {
                getGoogleAuthCredential(account)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }


    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        loginVM.signInWithGoogle(googleAuthCredential)
        loginVM.authenticatedUserLiveData.observe(this) { authenticatedUser ->
            if (authenticatedUser.isNew) {
                createNewUser(authenticatedUser)
            } else {
                goToKanbansActivity(authenticatedUser)
            }
        }
    }

    private fun createNewUser(authenticatedUser: User) {
        loginVM.createUser(authenticatedUser)
        loginVM.createdUserLiveData.observe(this) { user ->
            if (user.isCreated) {
                toastMessage(user.name)
                goToKanbansActivity(user)
            }
        }
    }

    private fun toastMessage(name: String) {
        Toast.makeText(this, "Hi $name!\nYour account was successfully created.", Toast.LENGTH_LONG)
            .show()
    }

    private fun goToKanbansActivity(user: User) {
        val intent = Intent(this, KanbansPageActivity::class.java)
        Log.w("icon", user.urlImage)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "GoogleActivity"
    }

}