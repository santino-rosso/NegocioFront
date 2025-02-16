package ar.edu.um.programacion2.trabajofinal.purchases.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.um.programacion2.trabajofinal.producto.domain.VentaRequest

@Composable
fun PurchasesScreen(viewModel: PurchasesViewModel, navController: NavController) {

  val ventas by viewModel.ventas.observeAsState(emptyList())
  val isLoading by viewModel.isLoading.observeAsState(false)

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
            text = "Compras realizadas",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
          )
          if (ventas.isEmpty()) {
            Text(
              text = "No has realizado ninguna compra.",
              fontSize = 16.sp,
              color = Color.Gray,
              modifier = Modifier.align(Alignment.CenterHorizontally),
              maxLines = Int.MAX_VALUE,
              overflow = TextOverflow.Visible
            )
            Spacer(modifier = Modifier.padding(16.dp))
            HomeButton(navController)
          } else {
            Compras(Modifier.align(Alignment.CenterHorizontally), ventas, navController)
          }
        }
      }
    }
  }
}

@Composable
fun Compras(modifier: Modifier, ventas: List<VentaRequest>, navController: NavController) {
  LazyColumn(modifier = modifier) {
    items(ventas) { venta ->
      VentaItem(venta, navController)
    }
    item {
      Spacer(modifier = Modifier.padding(16.dp))
      HomeButton(navController)
    }
  }
}

@Composable
fun VentaItem(venta: VentaRequest, navController: NavController) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp)
      .clickable { navController.navigate("venta/${venta.id}") },
    elevation = 4.dp
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(text = venta.nombre, fontSize = 20.sp, color = Color.Black)
      Text(text = "${venta.precioFinal} ${venta.moneda}", fontSize = 16.sp, color = Color.Gray)
      Text(text = venta.descripcion, fontSize = 14.sp, color = Color.DarkGray)
      Text(text = venta.fechaVenta, fontSize = 14.sp, color = Color.Black)
    }
  }
}

@Composable
fun HomeButton(navController: NavController) {
  Button(
    onClick = {
      navController.navigate("home") {
        popUpTo("purchases") { inclusive = true }
      }
    },
    modifier = Modifier.fillMaxWidth().height(48.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color(0xFF6200EE),
      contentColor = Color.White
    )
  ) {
    Text(text = "Volver al inicio")
  }
}
