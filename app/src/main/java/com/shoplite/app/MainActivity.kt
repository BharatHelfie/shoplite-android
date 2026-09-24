package com.shoplite.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shoplite.app.data.repository.AuthRepository
import com.shoplite.app.ui.cart.CartScreen
import com.shoplite.app.ui.detail.ProductDetailScreen
import com.shoplite.app.ui.home.HomeScreen
import com.shoplite.app.ui.login.LoginScreen
import com.shoplite.app.ui.theme.ShopLiteTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val start = if (authRepository.check()) "home" else "login"
        setContent {
            ShopLiteTheme {
                AppNav(start)
            }
        }
    }
}

@Composable
fun AppNav(start: String) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = start) {
        composable("login") {
            LoginScreen(onDone = {
                nav.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("home") {
            HomeScreen(
                onOpen = { id -> nav.navigate("detail/$id") },
                onCart = { nav.navigate("cart") },
                onLogout = { nav.navigate("login") }
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            ProductDetailScreen(
                id = entry.arguments!!.getInt("id"),
                onBack = { nav.popBackStack() },
                onCart = { nav.navigate("cart") }
            )
        }
        composable("cart") {
            CartScreen(onBack = { nav.popBackStack() })
        }
    }
}
