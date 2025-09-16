package com.jrexl.novanectorclient.modelview

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrexl.novanectorclient.apiinterface.ClientLogininterface
import com.jrexl.novanectorclient.dataclass.clientlogin
import com.jrexl.novanectorclient.objectapi.Retrofitclient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class loginclientmodelview : ViewModel() {
    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    fun login( userid: String, pass: String){
        viewModelScope.launch {
            try {
                val response = Retrofitclient.api.clientlog(clientlogin(userid, pass))
                if (response.isSuccessful) {
                    _loginResult.value = "true"
                } else {
                    _loginResult.value = "false"
                }
            } catch (e: Exception) {
                Log.d("loginerror", e.message.toString())
                _loginResult.value = "false" // also fail in case of network exception
            }
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }
}