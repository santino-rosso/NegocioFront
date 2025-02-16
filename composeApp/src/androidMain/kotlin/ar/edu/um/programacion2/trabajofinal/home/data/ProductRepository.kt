import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.home.domain.Dispositivo
import ar.edu.um.programacion2.trabajofinal.network.NetworkUtils
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ProductRepository(private val sessionManager: SessionManager) {

  private val url = "http://172.20.28.168:8080/api/dispositivos"

  suspend fun getProducts(): List<Dispositivo> {
    val token = sessionManager.fetchAuthToken().toString()
    val response = NetworkUtils.httpClient.get(url){
      contentType(ContentType.Application.Json)
      bearerAuth(token)
    }
    val responseBody = response.bodyAsText()
    return Json.decodeFromString<List<Dispositivo>>(responseBody)
  }

  suspend fun getProductById(id: String): Dispositivo {
    val token = sessionManager.fetchAuthToken().toString()
    val response = NetworkUtils.httpClient.get("$url/$id"){
      contentType(ContentType.Application.Json)
      bearerAuth(token)
    }
    val responseBody = response.bodyAsText()
    return Json.decodeFromString<Dispositivo>(responseBody)
  }
}
