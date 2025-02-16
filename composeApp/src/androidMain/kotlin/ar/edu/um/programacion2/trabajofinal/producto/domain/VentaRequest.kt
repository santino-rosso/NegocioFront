package ar.edu.um.programacion2.trabajofinal.producto.domain

import kotlinx.serialization.Serializable

@Serializable
data class VentaRequest(
  val id: Long? = null,
  val idDispositivo: Long,
  val codigo: String,
  val nombre: String,
  val descripcion: String,
  val precioBase: Double,
  val moneda: String,
  val caracteristicas: Set<CaracteristicaRequest> = emptySet(),
  val personalizaciones: Set<PersonalizacionOpcionRequest> = emptySet(),
  val adicionales: Set<AdicionalRequest> = emptySet(),
  val precioFinal: Double,
  val fechaVenta: String,
  val user: UserRequest?
)
