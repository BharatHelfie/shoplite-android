package com.shoplite.app.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onDone: () -> Unit,
    vm: LoginViewModel = hiltViewModel(),
) {
    if (vm.done) {
        onDone()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ShopLite", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3949AB))
        Text("Sign in to continue", color = Color.Gray)
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = vm.username,
            onValueChange = { vm.username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = vm.password,
            onValueChange = { vm.password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (vm.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(vm.error ?: "", color = Color.Red, fontSize = 13.sp)
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { vm.submit() },
            enabled = !vm.loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (vm.loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Log in")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Test accounts: emilys / emilyspass, michaelw / michaelwpass", color = Color.Gray, fontSize = 12.sp)
    }
}
