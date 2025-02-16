package ar.edu.um.programacion2.trabajofinal.producto.domain

import kotlinx.serialization.Serializable

@Serializable
data class PersonalizacionOpcionRequest(
  val id: Long? = null,
  val personalizacionId: Long,
  val nombre: String,
  val descripcion: String,
  val personalizacion: PersonalizacionRequest? = null,
  val opcion: OpcionRequest
)
