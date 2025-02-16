package ar.edu.um.programacion2.trabajofinal.home.domain

import kotlinx.serialization.Serializable

@Serializable
data class Caracteristica(
  val id: Long,
  val nombre: String,
  val descripcion: String
)
