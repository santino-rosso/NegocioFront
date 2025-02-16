package ar.edu.um.programacion2.trabajofinal.home.domain

import kotlinx.serialization.Serializable

@Serializable
data class Adicional(
  val id: Long,
  val nombre: String,
  val descripcion: String,
  val precio: Double,
  val precioGratis: Double,
)
