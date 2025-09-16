package com.jrexl.novanectorclient.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrexl.novanectorclient.dataclass.KycStatusRequest
import com.jrexl.novanectorclient.dataclass.Kycdataclss
import com.jrexl.novanectorclient.dataclass.kyccompletedata
import com.jrexl.novanectorclient.objectapi.kycretro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Kycmodelview: ViewModel() {

    private val _kycData = MutableStateFlow<List<Kycdataclss>?>(null)
    val kycdata: StateFlow<List<Kycdataclss>?> = _kycData

    fun kycprofileget(){
        viewModelScope.launch {
            try {
                val response = kycretro.kycapi.kycprofile()
                _kycData.value = response
            } catch (e: Exception) {
                _kycData.value = null
            }
        }}

    private val _kycDetail = MutableStateFlow<kyccompletedata?>(null)
    val kycDetail: StateFlow<kyccompletedata?> = _kycDetail

    fun kycindivdetaildata(contactno: String) {
        viewModelScope.launch {
            try {
                val response = kycretro.kycapi.kyccompleted(contactno)
                _kycDetail.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val kycStatusResult = MutableStateFlow<String?>(null)

    fun kycstatus(contactno: String, status: String){
        viewModelScope.launch {
            try {
               val response =  kycretro.kycapi.kycaccept(contactno, KycStatusRequest(status))
                if (response.isSuccessful) {
                    kycStatusResult.value = "KYC $status successful"
                } else {
                    kycStatusResult.value = "Failed: ${response.code()}"
                }            } catch (e: Exception) {
                e.printStackTrace()
                kycStatusResult.value = "Failed"

            }
        }

    }


}