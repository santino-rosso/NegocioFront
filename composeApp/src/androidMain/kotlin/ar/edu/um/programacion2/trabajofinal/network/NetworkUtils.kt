package ar.edu.um.programacion2.trabajofinal.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkUtils {
  val httpClient = HttpClient{
    install(ContentNegotiation){
      json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
    }
  }
}
