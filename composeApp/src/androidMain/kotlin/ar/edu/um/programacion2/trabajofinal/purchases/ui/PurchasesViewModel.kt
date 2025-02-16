package ar.edu.um.programacion2.trabajofinal.purchases.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.producto.data.VentaRepository
import ar.edu.um.programacion2.trabajofinal.producto.domain.VentaRequest
import kotlinx.coroutines.launch

class PurchasesViewModel(private val sessionManager: SessionManager) : ViewModel()  {

  private val ventaRepository = VentaRepository(sessionManager)

  private val _ventas = MutableLiveData<List<VentaRequest>>()
  val ventas: LiveData<List<VentaRequest>> = _ventas

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  init {
    fetchSales()
  }

  private fun fetchSales() {
    viewModelScope.launch {
      _isLoading.value = true
      try{
        _ventas.value = ventaRepository.getVentas()
        if (_ventas.value.isNullOrEmpty()) {
          _ventas.value = emptyList()
        }
      } catch (e: Exception) {
        e.printStackTrace()
        _ventas.value = emptyList()
      } finally {
        _isLoading.value = false
      }
    }
  }
}
