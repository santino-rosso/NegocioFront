package ar.edu.um.programacion2.trabajofinal.home.domain

import kotlinx.serialization.Serializable

@Serializable
data class Dispositivo(
  val id: Long,
  val codigo: String,
  val nombre: String,
  val descripcion: String,
  val precioBase: Double,
  val moneda: String,
  val caracteristicas: List<Caracteristica>,
  val personalizaciones: List<Personalizacion>,
  val adicionales: List<Adicional>
)
