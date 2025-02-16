package ar.edu.um.programacion2.trabajofinal.home.ui

import ProductRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.home.domain.Dispositivo
import kotlinx.coroutines.launch

class HomeViewModel(private val sessionManager: SessionManager) : ViewModel() {

  private val productRepository = ProductRepository(sessionManager)

  private val _dispositivos = MutableLiveData<List<Dispositivo>>()
  val dispositivos: LiveData<List<Dispositivo>> = _dispositivos

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _navigateToLogin = MutableLiveData<Boolean>()
  val navigateToLogin: LiveData<Boolean> = _navigateToLogin

  init {
    fetchProducts()
  }

  private fun fetchProducts() {
    viewModelScope.launch {
      _isLoading.value = true
      try{
        _dispositivos.value = productRepository.getProducts()
        if (_dispositivos.value.isNullOrEmpty()) {
          _dispositivos.value = emptyList()
        }
      } catch (e: Exception) {
        e.printStackTrace()
        _dispositivos.value = emptyList()
      } finally {
        _isLoading.value = false
      }
    }
  }

  fun logout() {
    sessionManager.clearAuthToken()
    sessionManager.clearUserId()
    _navigateToLogin.value = true
  }
}
