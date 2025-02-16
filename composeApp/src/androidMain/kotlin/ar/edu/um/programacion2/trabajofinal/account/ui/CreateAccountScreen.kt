package ar.edu.um.programacion2.trabajofinal.account.ui

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
import ar.edu.um.programacion2.trabajofinal.login.ui.HeaderImage
import ar.edu.um.programacion2.trabajofinal.login.ui.Login
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateAccountScreen(viewModel: CreateAccountViewModel, navController: NavController) {

  val scaffoldState = rememberScaffoldState()

  Scaffold(
    scaffoldState = scaffoldState,
    snackbarHost = { SnackbarHost(it) }
  ) {
    Box(Modifier.fillMaxSize().padding(16.dp)) {
      CreateAccount(Modifier.align(Alignment.Center), viewModel, navController, scaffoldState)
    }
  }
}

@Composable
fun CreateAccount(modifier: Modifier, viewModel: CreateAccountViewModel, navController: NavController, scaffoldState: ScaffoldState) {

  val login by viewModel.login.observeAsState("")
  val email by viewModel.email.observeAsState("")
  val password by viewModel.password.observeAsState("")
  val createAccountEnabled by viewModel.createAccountEnabled.observeAsState(false)
  val isLoading by viewModel.isLoading.observeAsState(false)
  val createAccountSuccess by viewModel.createAccountSuccess.observeAsState(false)
  val errorMessage by viewModel.errorMessage.observeAsState(null)
  val coroutineScope = rememberCoroutineScope()

  if (isLoading){
    Box(Modifier.fillMaxSize()){
      CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
  } else {
    Column(modifier = modifier) {
      HeaderImage(Modifier.align(Alignment.CenterHorizontally))
      Spacer(modifier = Modifier.padding(16.dp))
      LoginField(login) { viewModel.onCreateAccountChanged(it, email, password) }
      Spacer(modifier = Modifier.height(4.dp))
      EmailField(email) { viewModel.onCreateAccountChanged(login, it, password) }
      Spacer(modifier = Modifier.height(4.dp))
      PasswordField(password) { viewModel.onCreateAccountChanged(login, email, it) }
      Spacer(modifier = Modifier.padding(8.dp))
      LoginAccount(Modifier.align(Alignment.End), navController)
      Spacer(modifier = Modifier.height(16.dp))
      CreateAccountButton(createAccountEnabled) {
        coroutineScope.launch {
          viewModel.onCreateAccountSelected()
        }
      }
    }
  }

  if (createAccountSuccess) {
    LaunchedEffect(Unit) {
      scaffoldState.snackbarHostState.showSnackbar("Cuenta creada exitosamente")
      delay(2000)
      navController.navigate("login") {
        popUpTo("createAccount") { inclusive = true }
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
fun LoginField(login: String, onTextFieldChanged: (String) -> Unit) {
  TextField(
    value = login,
    onValueChange = {onTextFieldChanged(it)},
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
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
  TextField(
    value = email,
    onValueChange = {onTextFieldChanged(it)},
    modifier = Modifier.fillMaxWidth(),
    placeholder = { Text(text = "Mail") },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
  TextField(
    value = password,
    onValueChange = {onTextFieldChanged(it)},
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
fun CreateAccountButton(createAccountEnabled: Boolean, onCreateSelected: () -> Unit) {
  Button(
    onClick = { onCreateSelected() },
    modifier = Modifier.fillMaxWidth().height(48.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color(0xFFA80E8E),
      disabledBackgroundColor = Color(0xFFA80E8E),
      contentColor = Color.White,
      disabledContentColor = Color.White
    ), enabled = createAccountEnabled
  ) {
    Text("Crear cuenta")
  }
}

@Composable
fun HeaderImage(modifier: Modifier) {
  Image(
    painter = painterResource(id = R.drawable.ic_launcher_background),
    contentDescription = "Header",
    modifier = modifier
  )
}

@Composable
fun LoginAccount(modifier: Modifier, navController: NavController) {
  Text(
    text = "Iniciar sesión",
    modifier = modifier.clickable {
      navController.navigate("login")
    },
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    color = Color(0xFFA80E8E)
  )
}
