package ar.edu.um.programacion2.trabajofinal.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.um.programacion2.trabajofinal.home.domain.Dispositivo


@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {

  val dispositivos by viewModel.dispositivos.observeAsState(emptyList())
  val navigateToLogin by viewModel.navigateToLogin.observeAsState(false)
  val isLoading by viewModel.isLoading.observeAsState(false)

  if (navigateToLogin) {
    navController.navigate("login") {
      popUpTo("home") { inclusive = true }
    }
  }
  Box(Modifier.fillMaxSize().padding(16.dp)) {
    if (isLoading) {
      CircularProgressIndicator(Modifier.align(Alignment.Center))
    } else {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Productos disponibles",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
          )
          if (dispositivos.isEmpty()) {
            Text(
              text = "No hay productos disponibles. Por favor, intente más tarde.",
              fontSize = 16.sp,
              color = Color.Gray,
              modifier = Modifier.align(Alignment.CenterHorizontally),
              maxLines = Int.MAX_VALUE,
              overflow = TextOverflow.Visible
            )
            Spacer(modifier = Modifier.padding(16.dp))
            PurchasesButton(navController)
            Spacer(modifier = Modifier.padding(16.dp))
            LogoutButton(onLogoutSelected = { viewModel.logout() })
          } else {
            Home(Modifier.align(Alignment.CenterHorizontally), dispositivos, navController, viewModel)
          }
        }
      }
    }
  }
}

@Composable
fun Home(modifier: Modifier, dispositivos: List<Dispositivo>, navController: NavController, viewModel: HomeViewModel) {
  LazyColumn(modifier = modifier) {
    items(dispositivos) { dispositivo ->
      DispositivoItem(dispositivo, navController)
    }
    item {
      Spacer(modifier = Modifier.padding(16.dp))
      PurchasesButton(navController)
      Spacer(modifier = Modifier.padding(16.dp))
      LogoutButton(onLogoutSelected = { viewModel.logout() })
    }
  }
}

@Composable
fun DispositivoItem(dispositivo: Dispositivo, navController: NavController) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp)
      .clickable { navController.navigate("producto/${dispositivo.id}") },
    elevation = 4.dp
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(text = dispositivo.nombre, fontSize = 20.sp, color = Color.Black)
      Text(text = "${dispositivo.precioBase} ${dispositivo.moneda}", fontSize = 16.sp, color = Color.Gray)
      Text(text = dispositivo.descripcion, fontSize = 14.sp, color = Color.DarkGray)
    }
  }
}

@Composable
fun PurchasesButton(navController: NavController) {
  Button(
    onClick = {
      navController.navigate("purchases") {
        popUpTo("home") { inclusive = true }
      }
    },
    modifier = Modifier.fillMaxWidth().height(48.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color(0xFF6200EE),
      contentColor = Color.White
    )
  ) {
    Text(text = "Ver Compras Realizadas")
  }
}

@Composable
fun LogoutButton(onLogoutSelected: () -> Unit) {
  Button(
    onClick = { onLogoutSelected() },
    modifier = Modifier.fillMaxWidth().height(48.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color(0xFFA80E8E),
      contentColor = Color.White
    )
  ) {
    Text(text = "Cerrar sesión")
  }
}
