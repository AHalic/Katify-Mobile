package com.example.katify.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.katify.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


internal class LoginRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef: CollectionReference = rootRef.collection("users")

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<User> {
        val authenticatedUserMutableLiveData = MutableLiveData<User>()

        firebaseAuth.signInWithCredential(googleAuthCredential!!)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser =
                        authTask.result.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val photo = firebaseUser.photoUrl
                        val user = User(
                            uid,
                            name!!,
                            photo.toString(),
                            email!!
                        )
                        user.isNew = isNewUser
                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    authTask.exception!!.message?.let { Log.w("Repo", it) }
                }
            }
        return authenticatedUserMutableLiveData
    }

    fun createUserInFirestoreIfNotExists(authenticatedUser: User): MutableLiveData<User> {
        val newUserMutableLiveData = MutableLiveData<User>()
        val uidRef: DocumentReference = usersRef.document(authenticatedUser.userId)
        uidRef.get().addOnCompleteListener { uidTask ->
            if (uidTask.isSuccessful) {
                val document: DocumentSnapshot = uidTask.result
                if (!document.exists()) {
                    uidRef.set(authenticatedUser).addOnCompleteListener { userCreationTask ->
                        if (userCreationTask.isSuccessful) {
                            authenticatedUser.isCreated = true
                            newUserMutableLiveData.setValue(authenticatedUser)
                        } else {
                            Log.w("Repo", userCreationTask.exception)
                        }
                    }
                } else {
                    newUserMutableLiveData.setValue(authenticatedUser)
                }
            } else {
                Log.w("Repo", uidTask.exception)
            }
        }
        return newUserMutableLiveData
    }
}