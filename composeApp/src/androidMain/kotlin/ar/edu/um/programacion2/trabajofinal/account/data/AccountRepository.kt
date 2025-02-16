package ar.edu.um.programacion2.trabajofinal.account.data

import ar.edu.um.programacion2.trabajofinal.network.NetworkUtils
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AccountRepository() {

  private val url = "http://172.20.28.168:8080/api/register"

  suspend fun createAccount(login: String, email: String, password: String): HttpResponse {
    return NetworkUtils.httpClient.post(url) {
        contentType(ContentType.Application.Json)
        setBody(
          mapOf(
            "login" to login,
            "email" to email,
            "password" to password,
            "langKey" to "es"
          )
        )
    }
  }
}

