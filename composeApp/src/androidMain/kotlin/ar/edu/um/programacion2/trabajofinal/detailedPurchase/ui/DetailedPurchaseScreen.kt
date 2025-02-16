package ar.edu.um.programacion2.trabajofinal.detailedPurchase.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

@Composable
fun DetailedPurchaseScreen(viewModel: DetailedPurchaseViewModel, navController: NavController) {

  val venta by viewModel.venta.observeAsState(null)
  val isLoading by viewModel.isLoading.observeAsState(false)

  Box(Modifier.fillMaxSize().padding(16.dp)) {
    if (isLoading) {
      CircularProgressIndicator(Modifier.align(Alignment.Center))
    } else {
      venta?.let {
        VentaContent(it, navController)
      } ?: run {
        VentaNotFound(navController, Modifier.align(Alignment.Center))
      }
    }
  }
}

@Composable
fun VentaNotFound(navController: NavController, modifier: Modifier) {
  Text(
    text = "Venta no encontrada. Por favor, intente más tarde.",
    fontSize = 16.sp,
    color = Color.Gray,
    modifier = modifier,
    maxLines = Int.MAX_VALUE,
    overflow = TextOverflow.Visible
  )
  LaunchedEffect(Unit) {
    delay(4000)
    navController.navigate("purchases") {
      popUpTo("venta") { inclusive = true }
    }
  }
}

@Composable
fun VentaContent(venta: VentaRequest, navController: NavController) {

  Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
    VentaHeader(venta)
    Spacer(modifier = Modifier.height(16.dp))
    VentaCaracteristicas(venta)
    Spacer(modifier = Modifier.height(16.dp))
    VentaPersonalizaciones(venta)
    Spacer(modifier = Modifier.height(16.dp))
    VentaAdicionales(venta)
    Spacer(modifier = Modifier.height(16.dp))
    VentaPrecio(venta)
    Spacer(modifier = Modifier.height(16.dp))
    VentasButton(navController)
  }
}

@Composable
fun VentaHeader(venta: VentaRequest) {
  Text(text = venta.nombre, fontSize = 24.sp, color = Color.Black)
  Text(text = venta.descripcion, fontSize = 16.sp, color = Color.DarkGray)
  Text(text = "Fecha de venta: ${venta.fechaVenta}", fontSize = 16.sp, color = Color.Black)
}

@Composable
fun VentaCaracteristicas(venta: VentaRequest) {
  if (venta.caracteristicas.isNotEmpty()) {
    Text(text = "Características", fontSize = 20.sp, color = Color.Black)
    venta.caracteristicas.forEach { caracteristica ->
      Text(text = "- ${caracteristica.nombre}: ${caracteristica.descripcion}")
    }
  }
}

@Composable
fun VentaPersonalizaciones(venta: VentaRequest) {
  if (venta.personalizaciones.isNotEmpty()) {
    Text(text = "Personalizaciones", fontSize = 20.sp, color = Color.Black)
    venta.personalizaciones.forEach { personalizacion ->
      Text(text = "- ${personalizacion.nombre}: ${personalizacion.opcion.nombre} ")
      Text(text = "descripcion: ${personalizacion.opcion.descripcion}")
    }
  }
}

@Composable
fun VentaAdicionales(venta: VentaRequest) {
  if (venta.adicionales.isNotEmpty()) {
    Text(text = "Adicionales", fontSize = 20.sp, color = Color.Black)
    venta.adicionales.forEach { adicional ->
      Text(text = "- ${adicional.descripcion}")
    }
  }
}

@Composable
fun VentaPrecio(venta: VentaRequest) {
  Text(text = "Precio: ${venta.precioFinal} ${venta.moneda}", fontSize = 20.sp, color = Color.Black)
}

@Composable
fun VentasButton(navController: NavController) {
  Button(onClick = {
    navController.navigate("purchases") {
      popUpTo("venta") { inclusive = true }
    }
  },
    modifier = Modifier.fillMaxWidth().height(48.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color(0xFF6200EE),
      contentColor = Color.White
    )
  ) {
    Text(text = "Volver a mis compras")
  }
}
