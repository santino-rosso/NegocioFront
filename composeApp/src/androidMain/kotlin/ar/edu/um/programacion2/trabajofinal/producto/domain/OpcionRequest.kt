package ar.edu.um.programacion2.trabajofinal.producto.domain

import kotlinx.serialization.Serializable

@Serializable
data class OpcionRequest (
  val id: Long,
  val codigo: String,
  val nombre: String,
  val descripcion: String,
  val precioAdicional: Double
)

