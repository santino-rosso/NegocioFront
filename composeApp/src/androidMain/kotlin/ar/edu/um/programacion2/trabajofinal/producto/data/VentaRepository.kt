package ar.edu.um.programacion2.trabajofinal.producto.data

import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.network.NetworkUtils
import ar.edu.um.programacion2.trabajofinal.producto.domain.VentaRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class VentaRepository(private val sessionManager: SessionManager) {

  private val url = "http://172.20.28.168:8080/api/ventas"

  private val token = sessionManager.fetchAuthToken().toString()

  suspend fun registrarVenta(ventaRequest: VentaRequest): Boolean {
    val response = NetworkUtils.httpClient.post(url) {
      contentType(ContentType.Application.Json)
      bearerAuth(token)
      setBody(Json.encodeToString(ventaRequest))
    }
    if (response.status.value == 201) {
      return true
    } else {
      return false
    }
  }

  suspend fun getVentas(): List<VentaRequest> {
    val token = sessionManager.fetchAuthToken().toString()
    val id = sessionManager.fetchUserId().toString()
    val response = NetworkUtils.httpClient.get(url+"/user/$id") {
      contentType(ContentType.Application.Json)
      bearerAuth(token)
    }
    val responseBody = response.bodyAsText()
    return Json.decodeFromString<List<VentaRequest>>(responseBody)
  }

  suspend fun getVentaById(id: String): VentaRequest {
    val token = sessionManager.fetchAuthToken().toString()
    val response = NetworkUtils.httpClient.get(url+"/$id") {
      contentType(ContentType.Application.Json)
      bearerAuth(token)
    }
    val responseBody = response.bodyAsText()
    return Json.decodeFromString<VentaRequest>(responseBody)
  }
}
