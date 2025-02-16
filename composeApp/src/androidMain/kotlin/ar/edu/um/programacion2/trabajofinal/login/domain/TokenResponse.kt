package ar.edu.um.programacion2.trabajofinal.login.domain

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
  val id_token: String,
  val user_id: Long
)
