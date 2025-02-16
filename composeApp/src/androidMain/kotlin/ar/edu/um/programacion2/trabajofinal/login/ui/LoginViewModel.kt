package ar.edu.um.programacion2.trabajofinal.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.um.programacion2.trabajofinal.SessionManager
import ar.edu.um.programacion2.trabajofinal.login.data.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(sessionManager: SessionManager) : ViewModel(){

  private val loginRepository = LoginRepository(sessionManager)

  private val _username = MutableLiveData<String>()
  val username : LiveData<String> = _username

  private val _password = MutableLiveData<String>()
  val password : LiveData<String> = _password

  private val _loginEnabled = MutableLiveData<Boolean>()
  val loginEnabled : LiveData<Boolean> = _loginEnabled

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading : LiveData<Boolean> = _isLoading

  private val _loginSuccess = MutableLiveData<Boolean>()
  val loginSuccess: LiveData<Boolean> = _loginSuccess

  private val _errorMessage = MutableLiveData<String?>()
  val errorMessage: LiveData<String?> = _errorMessage

  init {
    _loginSuccess.value = false
  }

  fun onLoginChanged(username: String, password: String) {
    _username.value = username
    _password.value = password
    _loginEnabled.value = isValidUsername(username) && isValidPassword(password)
  }

  private fun isValidUsername(username: String): Boolean = username.length >= 3

  private fun isValidPassword(password: String): Boolean = password.length >= 3

  fun onLoginSelected() {
    _isLoading.value = true
    viewModelScope.launch {
      try {
        val success = loginRepository.loginApi(_username.value!!, _password.value!!)
        _isLoading.value = false
        if (success) {
          _loginSuccess.value = true
          _errorMessage.value = null
        } else {
          _errorMessage.value = "Usuario o contraseña incorrectos"
          resetState()
        }
      } catch (e: Exception) {
        _errorMessage.value = "Error al iniciar sesión"
        resetState()
      }
    }
  }

  private fun resetState() {
    _username.value = ""
    _password.value = ""
    _loginEnabled.value = false
    _isLoading.value = false
    _loginSuccess.value = false
  }
}

