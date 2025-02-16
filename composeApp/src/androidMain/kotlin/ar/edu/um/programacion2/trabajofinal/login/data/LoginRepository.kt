package ar.edu.um.programacion2.trabajofinal.login.data

import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.login.domain.TokenResponse
import ar.edu.um.programacion2.trabajofinal.network.NetworkUtils
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class LoginRepository(private val sessionManager: SessionManager) {

  private val url = "http://172.20.28.168:8080/api/authenticate"

  suspend fun loginApi(username :String, password :String): Boolean {
    val response = NetworkUtils.httpClient.post(url) {
      contentType(ContentType.Application.Json)
      setBody("""{ "username": "${username}", "password": "${password}", "rememberMe": false }""")
    }
    if (response.status.value == 200) {
      val responseBody = response.bodyAsText()
      val tokenResponse = Json.decodeFromString<TokenResponse>(responseBody)
      sessionManager.saveAuthToken(tokenResponse.id_token)
      sessionManager.saveUserId(tokenResponse.user_id)
    }
    return sessionManager.fetchAuthToken() != null && sessionManager.fetchUserId() != -1L
  }
}

