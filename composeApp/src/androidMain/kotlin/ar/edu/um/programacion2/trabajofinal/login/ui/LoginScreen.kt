package ar.edu.um.programacion2.trabajofinal.login.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.um.programacion2.trabajofinal.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {

  val scaffoldState = rememberScaffoldState()

  Scaffold(
    scaffoldState = scaffoldState,
    snackbarHost = { SnackbarHost(it) }
  ) {
    Box(Modifier.fillMaxSize().padding(16.dp)) {
      Login(Modifier.align(Alignment.Center), viewModel, navController, scaffoldState)
    }
  }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController, scaffoldState: ScaffoldState) {

  val username :String by viewModel.username.observeAsState(initial = "")
  val password :String by viewModel.password.observeAsState(initial = "")
  val loginEnabled :Boolean by viewModel.loginEnabled.observeAsState(initial = false)
  val isLoading :Boolean by viewModel.isLoading.observeAsState(initial = false)
  val loginSuccess by viewModel.loginSuccess.observeAsState(initial = false)
  val errorMessage by viewModel.errorMessage.observeAsState(initial = null)
  val coroutineScope = rememberCoroutineScope()

  if (isLoading) {
    Box(Modifier.fillMaxSize()){
      CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
  } else {
    Column(modifier = modifier) {
      HeaderImage(Modifier.align(Alignment.CenterHorizontally))
      Spacer(modifier = Modifier.padding(16.dp))
      UsernameField(username) { viewModel.onLoginChanged(it, password) }
      Spacer(modifier = Modifier.padding(4.dp))
      PasswordField(password) { viewModel.onLoginChanged(username, it) }
      Spacer(modifier = Modifier.padding(8.dp))
      CreateAccount(Modifier.align(Alignment.End), navController)
      Spacer(modifier = Modifier.padding(16.dp))
      LoginButton(loginEnabled) {
        coroutineScope.launch {
          viewModel.onLoginSelected()
        }
      }
    }
  }

  if (loginSuccess) {
    LaunchedEffect(Unit) {
      scaffoldState.snackbarHostState.showSnackbar("Inicio de sesión exitoso")
      delay(2000)
      navController.navigate("home") {
        popUpTo("login") { inclusive = true }
      }
    }
  } else {
    errorMessage?.let {
      LaunchedEffect(Unit) {
        scaffoldState.snackbarHostState.showSnackbar(it)
        delay(2000)
      }
    }
  }
}

@Composable
fun LoginButton(loginEnabled: Boolean, onLoginSelected: () -> Unit) {
  Button(onClick = { onLoginSelected() },
    modifier = Modifier.fillMaxWidth().height(48.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color(0xFFA80E8E),
      disabledBackgroundColor = Color(0xFFA80E8E),
      contentColor = Color.White,
      disabledContentColor = Color.White
    ), enabled = loginEnabled
  ) {
    Text(text = "Iniciar sesión")
  }
}

@Composable
fun CreateAccount(modifier: Modifier, navController: NavController) {
  Text(
    text = "Crear cuenta",
    modifier = modifier.clickable {
      navController.navigate("createAccount")
    },
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    color = Color(0xFFA80E8E)
  )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
  TextField(value = password, onValueChange = {onTextFieldChanged(it)},
    modifier = Modifier.fillMaxWidth(),
    placeholder = { Text(text = "Contraseña") },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    singleLine = true,
    maxLines = 1,
    colors = TextFieldDefaults.textFieldColors(
      textColor = Color(0xFFA80E8E),
      backgroundColor = Color(0xFFE1E1E1),
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent
    )
  )
}

@Composable
fun UsernameField(username: String, onTextFieldChanged: (String) -> Unit) {
  TextField(value = username, onValueChange = {onTextFieldChanged(it)},
    modifier = Modifier.fillMaxWidth(),
    placeholder = { Text(text = "Usuario") },
    singleLine = true,
    maxLines = 1,
    colors = TextFieldDefaults.textFieldColors(
      textColor = Color(0xFFA80E8E),
      backgroundColor = Color(0xFFE1E1E1),
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent
    )
  )
}

@Composable
fun HeaderImage(modifier: Modifier) {
  Image(
    painter = painterResource(id = R.drawable.um),
    contentDescription = "Header",
    modifier = modifier
  )
}
