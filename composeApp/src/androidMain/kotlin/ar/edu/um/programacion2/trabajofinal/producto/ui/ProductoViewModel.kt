package ar.edu.um.programacion2.trabajofinal.producto.ui

import ProductRepository
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.home.domain.Dispositivo
import ar.edu.um.programacion2.trabajofinal.producto.data.VentaRepository
import ar.edu.um.programacion2.trabajofinal.producto.domain.AdicionalRequest
import ar.edu.um.programacion2.trabajofinal.producto.domain.CaracteristicaRequest
import ar.edu.um.programacion2.trabajofinal.producto.domain.OpcionRequest
import ar.edu.um.programacion2.trabajofinal.producto.domain.PersonalizacionOpcionRequest
import ar.edu.um.programacion2.trabajofinal.producto.domain.PersonalizacionRequest
import ar.edu.um.programacion2.trabajofinal.producto.domain.UserRequest
import ar.edu.um.programacion2.trabajofinal.producto.domain.VentaRequest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter

class ProductoViewModel(sessionManager: SessionManager, id: String) : ViewModel() {

  private val sessionManager = sessionManager

  private val productRepository = ProductRepository(sessionManager)

  private val ventaReposito = VentaRepository(sessionManager)

  private val _dispositivo = MutableLiveData<Dispositivo?>()
  val dispositivo: LiveData<Dispositivo?> = _dispositivo

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _totalPrice = MutableLiveData<Double>()
  val totalPrice: LiveData<Double> = _totalPrice

  private val _ventaRealizada = MutableLiveData<Boolean>()
  val ventaRealizada: LiveData<Boolean> = _ventaRealizada

  init {
      fetchDispositivo(id)
  }

  private fun fetchDispositivo(dispositivoId: String?) {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        if (dispositivoId != null) {
          _dispositivo.value = productRepository.getProductById(dispositivoId)
          _totalPrice.value = _dispositivo.value?.precioBase
        }
      } catch (e: Exception) {
        e.printStackTrace()
        _dispositivo.value = null
      } finally {
        _isLoading.value = false
      }
    }
  }

  fun calculateTotalPrice(
    dispositivo: Dispositivo,
    selectedOptions: Map<Long, Int>,
    selectedAdicionales: Map<Long, Boolean>
  ) {
    var totalPrice = dispositivo.precioBase

    dispositivo.personalizaciones.forEach { personalizacion ->
      val selectedOptionIndex = selectedOptions[personalizacion.id] ?: 0
      val selectedOption = personalizacion.opciones[selectedOptionIndex]
      totalPrice += selectedOption.precioAdicional
    }

    var adicionalesPrice = 0.00

    dispositivo.adicionales.forEach { adicional ->
      if (selectedAdicionales[adicional.id] == true) {
        if (totalPrice < adicional.precioGratis!! || adicional.precioGratis == -1.00) {
          adicionalesPrice += adicional.precio!!
        }
      }
    }

    totalPrice += adicionalesPrice
    _totalPrice.value = totalPrice
  }


  @RequiresApi(Build.VERSION_CODES.O)
  fun createVentaRequest(
    dispositivo: Dispositivo,
    selectedOptions: Map<Long, Int>,
    selectedAdicionales: Map<Long, Boolean>,
    totalPrice: Double
  ): VentaRequest {
    val caracteristicas = dispositivo.caracteristicas.map {
      CaracteristicaRequest(it.id, it.nombre, it.descripcion)
    }.toSet()

    val personalizaciones = dispositivo.personalizaciones.map { personalizacion ->
      val selectedOptionIndex = selectedOptions[personalizacion.id] ?: 0
      val selectedOption = personalizacion.opciones[selectedOptionIndex]
      val opcionRequest = OpcionRequest(selectedOption.id, selectedOption.codigo, selectedOption.nombre, selectedOption.descripcion, selectedOption.precioAdicional)
      val personalizacionRequest = PersonalizacionRequest(personalizacion.id, personalizacion.nombre, personalizacion.descripcion)
      PersonalizacionOpcionRequest(null,personalizacion.id, personalizacion.nombre, personalizacion.descripcion, personalizacionRequest, opcionRequest)
    }.toSet()

    val adicionales = dispositivo.adicionales.filter {
      selectedAdicionales[it.id] == true
    }.map {
      AdicionalRequest(it.id, it.nombre, it.descripcion, it.precio)
    }.toSet()

    val instant = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS)
    val fecha = DateTimeFormatter.ISO_INSTANT.format(instant)

    return VentaRequest(
      idDispositivo = dispositivo.id,
      codigo = dispositivo.codigo,
      nombre = dispositivo.nombre,
      descripcion = dispositivo.descripcion,
      precioBase = dispositivo.precioBase,
      moneda = dispositivo.moneda,
      caracteristicas = caracteristicas,
      personalizaciones = personalizaciones,
      adicionales = adicionales,
      precioFinal = totalPrice,
      fechaVenta = fecha,
      user = UserRequest(sessionManager.fetchUserId())
    )
  }

  fun registrarVenta(ventaRequest: VentaRequest) {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        _ventaRealizada.value = ventaReposito.registrarVenta(ventaRequest)
      } catch (e: Exception) {
        e.printStackTrace()
        _ventaRealizada.value = false
      } finally {
        _isLoading.value = false
      }
    }
  }

}
