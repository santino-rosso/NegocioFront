package ar.edu.um.programacion2.trabajofinal.detailedPurchase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.home.domain.Dispositivo
import ar.edu.um.programacion2.trabajofinal.producto.data.VentaRepository
import ar.edu.um.programacion2.trabajofinal.producto.domain.VentaRequest
import kotlinx.coroutines.launch

class DetailedPurchaseViewModel(sessionManager: SessionManager, id: String) : ViewModel()  {

  private val ventaReposito = VentaRepository(sessionManager)

  private val _venta = MutableLiveData<VentaRequest?>()
  val venta: LiveData<VentaRequest?> = _venta

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  init {
    fetchSale(id)
  }

  private fun fetchSale(Id: String?) {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        if (Id != null) {
          _venta.value = ventaReposito.getVentaById(Id)
        }
      } catch (e: Exception) {
        e.printStackTrace()
        _venta.value = null
      } finally {
        _isLoading.value = false
      }
    }
  }

}
