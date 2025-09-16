package com.jrexl.novanectorclient.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrexl.novanectorclient.dataclass.HomePagedataclass
import com.jrexl.novanectorclient.dataclass.Hometransactiondataclass
import com.jrexl.novanectorclient.objectapi.HomePageRetro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.os.Environment
import com.jrexl.novanectorclient.dataclass.qrcheckdb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class HomePageviewmodel: ViewModel() {



    private val _qrCheckResult = MutableStateFlow<qrcheckdb?>(null)
    val qrCheckResult: StateFlow<qrcheckdb?> = _qrCheckResult
fun checkqrmv(scanQRCodes: String){
    viewModelScope.launch {
        try {
            val res = HomePageRetro.api.checkqrcodes(scanQRCodes)
            if (res.isSuccessful) {
                _qrCheckResult.value = res.body()
            } else {
                _qrCheckResult.value = qrcheckdb(false)
            }

        }catch (e: Exception){
            print(e)
        }
    }
}


    private val _userData = MutableStateFlow<HomePagedataclass?>(null)
    val userData: StateFlow<HomePagedataclass?> = _userData

    fun loadUserData() {
        viewModelScope.launch {
            try {
                val result = HomePageRetro.api.gethomedata()
                _userData.value = result
            } catch (e: Exception) {
                _userData.value = null
            }
        }
    }



    private val _trans = MutableStateFlow<List<Hometransactiondataclass>>(emptyList())
    val transdata: StateFlow<List<Hometransactiondataclass>> = _trans
    fun hometransaction(){
        viewModelScope.launch {
            try {
                val result = HomePageRetro.api.hometransactiondewtails()
                _trans.value = result
            } catch (e: Exception) {
               _trans.value = emptyList()
            }
        }

    }
}