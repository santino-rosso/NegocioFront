package ar.edu.um.programacion2.trabajofinal.account.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.um.programacion2.trabajofinal.account.data.AccountRepository
import ar.edu.um.programacion2.trabajofinal.account.domain.RegisterErrorResponse
import io.ktor.client.call.body
import kotlinx.coroutines.launch

class CreateAccountViewModel() : ViewModel() {

  private val accountRepository = AccountRepository()

  private val _login = MutableLiveData<String>()
  val login: LiveData<String> = _login

  private val _email = MutableLiveData<String>()
  val email: LiveData<String> = _email

  private val _password = MutableLiveData<String>()
  val password: LiveData<String> = _password

  private val _createAccountEnabled = MutableLiveData<Boolean>()
  val createAccountEnabled: LiveData<Boolean> = _createAccountEnabled

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _createAccountSuccess = MutableLiveData<Boolean>()
  val createAccountSuccess: LiveData<Boolean> = _createAccountSuccess

  private val _errorMessage = MutableLiveData<String?>()
  val errorMessage: LiveData<String?> = _errorMessage

  private val errorMessages = mapOf(
    "error.userexists" to "Usuario no disponible.",
    "error.emailexists" to "Mail ya utilizado para otra cuenta."
  )

  init {
    _createAccountSuccess.value = false
  }

  fun onCreateAccountChanged(login: String, email: String, password: String) {
    _login.value = login
    _email.value = email
    _password.value = password
    _createAccountEnabled.value = isValidLogin(login) && isValidEmail(email) && isValidPassword(password)
  }

  private fun isValidLogin(login: String): Boolean = login.length >= 3

  private fun isValidEmail(email: String): Boolean = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email)

  private fun isValidPassword(password: String): Boolean = password.length >= 3

  fun onCreateAccountSelected() {
    _isLoading.value = true
    viewModelScope.launch {
      try {
        val response = accountRepository.createAccount(_login.value!!, _email.value!!, _password.value!!)
        _isLoading.value = false
        if (response.status.value == 201) {
          _createAccountSuccess.value = true
          _errorMessage.value = null
        } else {
          val errorResponse: RegisterErrorResponse = response.body()
          _errorMessage.value = errorMessages[errorResponse.message] ?: "Error al crear la cuenta: ${errorResponse.title}"
          resetState()
        }
      } catch (e: Exception) {
        _errorMessage.value = "Error al crear la cuenta"
        resetState()
      }
    }
  }

  private fun resetState() {
    _login.value = ""
    _email.value = ""
    _password.value = ""
    _createAccountEnabled.value = false
    _isLoading.value = false
    _createAccountSuccess.value = false
  }
}
