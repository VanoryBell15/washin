package com.example.washinton.feature.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        _state.value = SignUpState.Loading

        val auth = FirebaseAuth.getInstance()
        val database = Firebase.database.reference

        // Firebase Auth: Register the user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user

                    user?.let {
                        // Update user's display name in Firebase Authentication
                        it.updateProfile(
                            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                        ).addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                // Create an entry for the user in Realtime Database
                                val userData = mapOf(
                                    "profile" to mapOf(
                                        "user_id" to it.uid,
                                        "name" to name
                                    ),
                                )

                                database.child("users").child(it.uid)
                                    .setValue(userData)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            // Sign in the user after database entry is successful
                                            auth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener { signInTask ->
                                                    if (signInTask.isSuccessful) {
                                                        _state.value = SignUpState.Success
                                                    } else {
                                                        _state.value = SignUpState.Error
                                                    }
                                                }
                                        } else {
                                            _state.value = SignUpState.Error
                                        }
                                    }
                            } else {
                                _state.value = SignUpState.Error
                            }
                        }
                    } ?: run {
                        _state.value = SignUpState.Error
                    }
                } else {
                    _state.value = SignUpState.Error
                }
            }
    }
}




sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Error: SignUpState()
    object Success : SignUpState()

}