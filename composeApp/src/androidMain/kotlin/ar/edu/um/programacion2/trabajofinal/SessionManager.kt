package ar.edu.um.programacion2.trabajofinal

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

  private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

  companion object {
    private const val TOKEN_KEY = "auth_token"
    private const val USER_ID_KEY = "user_id"
  }

  fun saveAuthToken(token: String) {
    val editor = prefs.edit()
    editor.putString(TOKEN_KEY, token)
    editor.apply()
  }

  fun fetchAuthToken(): String? {
    return prefs.getString(TOKEN_KEY, null)
  }

  fun clearAuthToken() {
    prefs.edit().remove(TOKEN_KEY).apply()
  }

  fun saveUserId(userId: Long) {
    val editor = prefs.edit()
    editor.putLong(USER_ID_KEY, userId)
    editor.apply()
  }

  fun fetchUserId(): Long {
    return prefs.getLong(USER_ID_KEY, -1)
  }

  fun clearUserId() {
    prefs.edit().remove(USER_ID_KEY).apply()
  }
}
