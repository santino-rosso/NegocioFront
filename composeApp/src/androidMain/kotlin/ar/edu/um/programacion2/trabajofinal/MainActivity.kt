package ar.edu.um.programacion2.trabajofinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.um.programacion2.trabajofinal.account.ui.CreateAccountScreen
import ar.edu.um.programacion2.trabajofinal.account.ui.CreateAccountViewModel
import ar.edu.um.programacion2.trabajofinal.detailedPurchase.ui.DetailedPurchaseScreen
import ar.edu.um.programacion2.trabajofinal.detailedPurchase.ui.DetailedPurchaseViewModel
import ar.edu.um.programacion2.trabajofinal.home.ui.HomeScreen
import ar.edu.um.programacion2.trabajofinal.home.ui.HomeViewModel
import ar.edu.um.programacion2.trabajofinal.login.ui.LoginScreen
import ar.edu.um.programacion2.trabajofinal.login.ui.LoginViewModel
import ar.edu.um.programacion2.trabajofinal.producto.ui.ProductoScreen
import ar.edu.um.programacion2.trabajofinal.producto.ui.ProductoViewModel
import ar.edu.um.programacion2.trabajofinal.purchases.ui.PurchasesScreen
import ar.edu.um.programacion2.trabajofinal.purchases.ui.PurchasesViewModel

class MainActivity : ComponentActivity() {

  private lateinit var sessionManager: SessionManager

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sessionManager = SessionManager(applicationContext)
    setContent {
      MyApp(sessionManager)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    sessionManager.clearAuthToken()
    sessionManager.clearUserId()
  }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(sessionManager: SessionManager) {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = "login") {
    composable("login") {
      LoginScreen(LoginViewModel(sessionManager), navController)
    }
    composable("home") {
      HomeScreen(HomeViewModel(sessionManager), navController)
    }
    composable("producto/{dispositivoId}") { backStackEntry ->
      val dispositivoId = backStackEntry.arguments?.getString("dispositivoId")
      ProductoScreen(ProductoViewModel(sessionManager, dispositivoId.toString()), navController)
    }
    composable("createAccount") {
      CreateAccountScreen(CreateAccountViewModel(), navController)
    }
    composable("purchases"){
      PurchasesScreen(PurchasesViewModel(sessionManager),navController)
    }
    composable("venta/{id}") { backStackEntry ->
      val Id = backStackEntry.arguments?.getString("id")
      DetailedPurchaseScreen(DetailedPurchaseViewModel(sessionManager, Id.toString()), navController)
    }
  }
}


