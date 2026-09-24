package com.shoplite.app.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplite.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    var username by mutableStateOf("emilys")
    var password by mutableStateOf("emilyspass")
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var done by mutableStateOf(false)

    // Validates the input and logs the user in
    fun submit() {
        if (username.isBlank() || password.isBlank()) {
            error = "Please enter username and password"
            return
        }
        loading = true
        error = null
        viewModelScope.launch {
            try {
                authRepository.go(username, password)
                done = true
            } catch (e: Exception) {
                error = e.message
            }
            loading = false
        }
    }
}
