package ar.edu.um.programacion2.trabajofinal.producto.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import ar.edu.um.programacion2.trabajofinal.home.domain.Dispositivo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductoScreen(viewModel: ProductoViewModel, navController: NavController) {
  val dispositivo by viewModel.dispositivo.observeAsState(null)
  val isLoading by viewModel.isLoading.observeAsState(false)
  val ventaRealizada by viewModel.ventaRealizada.observeAsState(false)

  Box(Modifier.fillMaxSize().padding(16.dp)) {
    if (isLoading) {
      CircularProgressIndicator(Modifier.align(Alignment.Center))
    } else if (ventaRealizada) {
      VentaContent(navController, Modifier.align(Alignment.Center))
    } else {
      dispositivo?.let {
        ProductoContent(it, viewModel, navController)
      } ?: run {
        ProductoNotFound(navController, Modifier.align(Alignment.Center))
      }
    }
  }
}

@Composable
fun ProductoNotFound(navController: NavController, modifier: Modifier) {
  Text(
    text = "Producto no encontrado. Por favor, intente más tarde.",
    fontSize = 16.sp,
    color = Color.Gray,
    modifier = modifier,
    maxLines = Int.MAX_VALUE,
    overflow = TextOverflow.Visible
  )
  LaunchedEffect(Unit) {
    delay(4000)
    navController.navigate("home") {
      popUpTo("producto") { inclusive = true }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductoContent(dispositivo: Dispositivo, viewModel: ProductoViewModel, navController: NavController) {
  var selectedOptions = remember { mutableStateMapOf<Long, Int>() }
  var selectedAdicionales = remember { mutableStateMapOf<Long, Boolean>() }
  val totalPrice by viewModel.totalPrice.observeAsState(dispositivo.precioBase.toDouble())

  Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
    ProductoHeader(dispositivo)
    Spacer(modifier = Modifier.height(16.dp))
    ProductoCaracteristicas(dispositivo)
    Spacer(modifier = Modifier.height(16.dp))
    ProductoPersonalizaciones(dispositivo, selectedOptions, viewModel, selectedAdicionales)
    Spacer(modifier = Modifier.height(16.dp))
    ProductoAdicionales(dispositivo, selectedAdicionales, viewModel, selectedOptions)
    Spacer(modifier = Modifier.height(16.dp))
    ProductoPrecio(totalPrice, dispositivo)
    Spacer(modifier = Modifier.height(16.dp))
    ProductoAcciones(navController, dispositivo, selectedOptions, selectedAdicionales, totalPrice, viewModel)
  }
}

@Composable
fun ProductoHeader(dispositivo: Dispositivo) {
  Text(text = dispositivo.nombre, fontSize = 24.sp, color = Color.Black)
  Text(text = dispositivo.descripcion, fontSize = 16.sp, color = Color.DarkGray)
}

@Composable
fun ProductoCaracteristicas(dispositivo: Dispositivo) {
  if (dispositivo.caracteristicas.isNotEmpty()) {
    Text(text = "Características", fontSize = 20.sp, color = Color.Black)
    dispositivo.caracteristicas.forEach { caracteristica ->
      Text(text = "- ${caracteristica.nombre}: ${caracteristica.descripcion}")
    }
  }
}

@Composable
fun ProductoPersonalizaciones(
  dispositivo: Dispositivo,
  selectedOptions: MutableMap<Long, Int>,
  viewModel: ProductoViewModel,
  selectedAdicionales: MutableMap<Long, Boolean>
) {
  if (dispositivo.personalizaciones.isNotEmpty()) {
    Text(text = "Personalizaciones", fontSize = 20.sp, color = Color.Black)
    dispositivo.personalizaciones.forEach { personalizacion ->
      var selectedOption by remember { mutableStateOf(0) }
      Text(text = personalizacion.descripcion, fontSize = 18.sp, color = Color.Black)
      personalizacion.opciones.forEachIndexed { index, opcion ->
        Row(verticalAlignment = Alignment.CenterVertically) {
          RadioButton(
            selected = (selectedOption == index),
            onClick = {
              selectedOption = index
              selectedOptions[personalizacion.id] = index
              viewModel.calculateTotalPrice(dispositivo, selectedOptions, selectedAdicionales)
            }
          )
          Text(text = opcion.nombre.toString() + " - " + opcion.descripcion.toString())
        }
      }
    }
  }
}

@Composable
fun ProductoAdicionales(
  dispositivo: Dispositivo,
  selectedAdicionales: MutableMap<Long, Boolean>,
  viewModel: ProductoViewModel,
  selectedOptions: MutableMap<Long, Int>
) {
  if (dispositivo.adicionales.isNotEmpty()) {
    Text(text = "Adicionales", fontSize = 20.sp, color = Color.Black)
    dispositivo.adicionales.forEach { adicional ->
      var isSelected by remember { mutableStateOf(false) }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
          checked = isSelected,
          onCheckedChange = {
            isSelected = it
            selectedAdicionales[adicional.id] = it
            viewModel.calculateTotalPrice(dispositivo, selectedOptions, selectedAdicionales)
          }
        )
        Text(text = "${adicional.nombre}: ${adicional.descripcion}")
      }
    }
  }
}

@Composable
fun ProductoPrecio(totalPrice: Double, dispositivo: Dispositivo) {
  Text(text = "Precio: $totalPrice ${dispositivo.moneda}", fontSize = 20.sp, color = Color.Black)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductoAcciones(
  navController: NavController,
  dispositivo: Dispositivo,
  selectedOptions: Map<Long, Int>,
  selectedAdicionales: Map<Long, Boolean>,
  totalPrice: Double,
  viewModel: ProductoViewModel,
  coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    Button(onClick = {
      coroutineScope.launch {
        val ventaRequest = viewModel.createVentaRequest(dispositivo, selectedOptions, selectedAdicionales, totalPrice)
        viewModel.registrarVenta(ventaRequest)
      }
    }) {
      Text(text = "Comprar")
    }
    Button(onClick = { navController.navigate("home") {
      popUpTo("producto") { inclusive = true }
    } }) {
      Text(text = "Cancelar")
    }
  }
}

@Composable
fun VentaContent(navController: NavController, modifier: Modifier) {
  Text(
    text = "Compra realizada con éxito.",
    fontSize = 16.sp,
    color = Color.Green,
    modifier = modifier,
    maxLines = Int.MAX_VALUE,
    overflow = TextOverflow.Visible
  )
  LaunchedEffect(Unit) {
    delay(4000)
    navController.navigate("home") {
      popUpTo("producto") { inclusive = true }
    }
  }
}
