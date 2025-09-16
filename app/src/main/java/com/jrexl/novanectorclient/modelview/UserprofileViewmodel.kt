package com.jrexl.novanectorclient.modelview

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrexl.novanectorclient.dataclass.ProfileDataClass
import com.jrexl.novanectorclient.dataclass.UserProfiledataclass
import com.jrexl.novanectorclient.objectapi.Profileretro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserprofileViewmodel: ViewModel() {

    var profilevm by mutableStateOf<ProfileDataClass?>(null)
        private set
    @SuppressLint("SuspiciousIndentation")
    fun profiledatagetviewmode(contactno: String?) {
        viewModelScope.launch {
            try {
                val response = Profileretro.api.Profileget(contactno)
                if (response.isSuccessful) {
                    profilevm = response.body()
                } else {
                    Log.e("API", "Error: ${response.code()} ${response.message()}")
                    profilevm = null
                }
            }catch (e : Exception){
                print(e)
            }
        }

    }

    private val _userData = MutableStateFlow<List<UserProfiledataclass>?>(null)
    val profiledata: StateFlow<List<UserProfiledataclass>?> = _userData

    suspend fun loadUserData() {
        viewModelScope.launch {
            try {
                val response = Profileretro.api.clientprofile()
                _userData.value = response
            } catch (e: Exception) {
                _userData.value = null
            }
        }


    }

}