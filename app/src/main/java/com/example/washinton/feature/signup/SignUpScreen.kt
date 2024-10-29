package com.example.washinton.feature.signup


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.washinton.R
import com.example.washinton.ui.theme.DarkBlue
import com.example.washinton.ui.theme.LightBlue

@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
            is SignUpState.Success -> {
                navController.navigate("Home")
            }
            is SignUpState.Error -> {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
            else -> {

            }
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(DarkBlue)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Box(modifier = Modifier.clip(RoundedCornerShape(size = 120.dp)).size(140.dp)){
                Image(
                    painter = painterResource(id = R.drawable.washinton_logo_small),
                    contentDescription = "WASHinton Logo",
                    modifier = Modifier
                        .background(Color.White)
                        .size(140.dp)
                )}

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier.fillMaxWidth(),value = firstName, onValueChange = {firstName = it}, label = { Text(text = "First Name",) })

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier.fillMaxWidth(),value = lastName, onValueChange = {lastName = it}, label = { Text(text = "Last Name",) })

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier.fillMaxWidth(),value = email, onValueChange = {email = it}, label = { Text(text = "Email",) })

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier.fillMaxWidth(),value = password, onValueChange = {password = it}, visualTransformation = PasswordVisualTransformation(), label = { Text(text = "Password") })

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier.fillMaxWidth(),value = confirm, onValueChange = {confirm = it}, visualTransformation = PasswordVisualTransformation(), isError = password.isNotEmpty() && confirm.isNotEmpty() && password != confirm, label = { Text(text = "Confirm Password") })

            Spacer(modifier = Modifier.height(16.dp))

            if(uiState.value == SignUpState.Loading){
                CircularProgressIndicator()
            }else {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue,
                        contentColor = Color.White
                    ), onClick = { viewModel.signUp("$firstName $lastName", email, password) },
                    enabled = password.isNotEmpty() && confirm.isNotEmpty() && password == confirm && firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()
                ) {
                    Text(text = "Sign Up", color = Color.White)
                }

                TextButton (onClick = { navController.navigate("login") }) {
                    Text(text = "Have an account? Sign In", color = Color.White)
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}