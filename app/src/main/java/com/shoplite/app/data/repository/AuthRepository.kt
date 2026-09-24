package com.shoplite.app.data.repository

import android.content.Context
import com.shoplite.app.network.ApiService
import com.shoplite.app.network.model.LoginRequest
import com.shoplite.app.network.model.LoginResponse
import com.shoplite.app.network.model.RefreshRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiService,
) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Logs the user in with username and password and saves the session
    suspend fun go(u: String, p: String): LoginResponse {
        val res = api.login(LoginRequest(u, p, 30))
        prefs.edit()
            .putString("token", res.accessToken)
            .putString("refresh_token", res.refreshToken)
            .putInt("user_id", res.id)
            .putString("user_name", res.firstName)
            .commit()
        return res
    }

    // Returns true if the user is logged in and the token has not expired
    fun check(): Boolean {
        return prefs.getString("token", null) != null
    }

    // Refreshes the access token using the refresh token
    suspend fun update(): Boolean {
        val refresh = prefs.getString("refreshToken", null) ?: return false
        val res = api.refresh(RefreshRequest(refresh, 30))
        if (res.isSuccessful) {
            prefs.edit().putString("token", res.body()?.accessToken).apply()
            return true
        }
        return false
    }

    // Returns the logged in user's id
    fun uid(): Int = prefs.getInt("user_id", 1)

    // Logs the user out and clears all user data
    fun clear() {
        prefs.edit().remove("token").apply()
    }
}
