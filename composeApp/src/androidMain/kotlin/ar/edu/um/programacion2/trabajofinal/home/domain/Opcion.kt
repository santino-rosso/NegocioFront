package ar.edu.um.programacion2.trabajofinal.home.domain

import kotlinx.serialization.Serializable

@Serializable
data class Opcion(
  val id: Long,
  val codigo: String,
  val nombre: String,
  val descripcion: String,
  val precioAdicional: Double
)
