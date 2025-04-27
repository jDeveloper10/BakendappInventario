package com.example.inventario3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventario3.ui.screens.HomeScreen
import com.example.inventario3.ui.screens.LoginScreen
import com.example.inventario3.ui.screens.RegisterScreen
import com.example.inventario3.ui.viewmodels.AuthViewModel

/**
 * Rutas de navegación para la aplicación
 */
object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

/**
 * Componente principal de navegación que gestiona las rutas de la aplicación
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState(initial = false)
    val startDestination = if (isAuthenticated) AppRoutes.HOME else AppRoutes.LOGIN
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de inicio de sesión
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }
        
        // Pantalla de registro
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        // Pantalla principal
        composable(AppRoutes.HOME) {
            HomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}