package com.example.katify.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import com.example.katify.data.LoginRepository
import com.example.katify.data.User

class LoginViewModel : ViewModel() {
    private var authRepository: LoginRepository = LoginRepository()
    lateinit var authenticatedUserLiveData : LiveData<User>
    lateinit var createdUserLiveData : LiveData<User>

    fun signInWithGoogle(googleAuthCredential : AuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential)
    }

    fun createUser(authenticatedUser : User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser)
    }
}