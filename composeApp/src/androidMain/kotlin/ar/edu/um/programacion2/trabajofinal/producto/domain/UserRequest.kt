package ar.edu.um.programacion2.trabajofinal.producto.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest (
  val id: Long,
  val login: String? = null,
)
