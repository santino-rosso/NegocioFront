package ar.edu.um.programacion2.trabajofinal.producto.domain

import kotlinx.serialization.Serializable

@Serializable
data class PersonalizacionRequest(
  val id: Long,
  val nombre: String,
  val descripcion: String
)

