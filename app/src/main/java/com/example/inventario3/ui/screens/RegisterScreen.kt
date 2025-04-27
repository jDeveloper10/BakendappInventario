package com.example.inventario3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventario3.ui.viewmodels.LoginViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(loginState) {
        if (loginState is LoginViewModel.LoginState.Success) {
            onRegisterSuccess()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de Usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre Completo (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                if (confirmPassword.isNotEmpty()) {
                    passwordError = if (it != confirmPassword) "Las contraseñas no coinciden" else null
                }
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                passwordError = if (password != it) "Las contraseñas no coinciden" else null
            },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        if (loginState is LoginViewModel.LoginState.Error) {
            Text(
                text = (loginState as LoginViewModel.LoginState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        Button(
            onClick = { 
                if (password == confirmPassword) {
                    viewModel.register(username, email, password, fullName.takeIf { it.isNotBlank() })
                } else {
                    passwordError = "Las contraseñas no coinciden"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading && 
                     username.isNotEmpty() && 
                     email.isNotEmpty() && 
                     password.isNotEmpty() &&
                     confirmPassword.isNotEmpty() &&
                     passwordError == null
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Registrarse")
            }
        }
        
        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("¿Ya tienes cuenta? Iniciar Sesión")
        }
    }
}