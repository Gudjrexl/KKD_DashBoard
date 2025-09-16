package com.jrexl.novanectorclient.modelview

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrexl.novanectorclient.dataclass.Cardinfo
import com.jrexl.novanectorclient.dataclass.CreditCoinRequest
import com.jrexl.novanectorclient.dataclass.DeductCoinRequest
import com.jrexl.novanectorclient.dataclass.Withdrawaldtclss
import com.jrexl.novanectorclient.dataclass.alltransactiondc
import com.jrexl.novanectorclient.dataclass.transactionhistory
import com.jrexl.novanectorclient.dataclass.withdrawalresponse
import com.jrexl.novanectorclient.objectapi.Withdrawalobject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WithdrawalModelView: ViewModel() {

    var cardInfo by mutableStateOf<Cardinfo?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    fun fetchCardInfo(phone: String){
        viewModelScope.launch {
            try {
                isLoading = true
                val infoResult = Withdrawalobject.api.getCardInfo(phone)
                cardInfo = infoResult
            }catch (e : Exception){
                e.printStackTrace()
            }
            finally {
                isLoading = false
            }

        }
    }



    private val _creditResult = MutableStateFlow<String?>(null)
    val creditResult: StateFlow<String?> = _creditResult

    private val _deductResult = MutableStateFlow<String?>(null)
    val deductResult: StateFlow<String?> = _deductResult

    fun credit(contactcno: String, coins: Int, scanQRCodes: String) {
        viewModelScope.launch {
            try {
                val response = Withdrawalobject.api.creditCoin(
                    CreditCoinRequest(contactcno, coins, scanQRCodes)
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    _creditResult.value = "Credit Success}"

                } else {
                    _creditResult.value = "Credit failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _creditResult.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    fun debit(contactcno: String, coins: Int, reason: String) {
        viewModelScope.launch {
            try {
                val response = Withdrawalobject.api.deductCoin(
                    DeductCoinRequest(contactcno, coins, reason)
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    _deductResult.value = "Deduct failed:"
                } else {
                    _deductResult.value = "Deduct failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _deductResult.value = "Error: ${e.localizedMessage}"
            }
        }
    }



    private val _transactionsList = MutableStateFlow<List<transactionhistory>>(emptyList())
    val transactionsList: StateFlow<List<transactionhistory>> = _transactionsList

    fun transactionhistorymodelfunc(contactcno: String?){
        viewModelScope.launch {
            try {
                var transactionhistorymodel = Withdrawalobject.api.getTransactions(contactcno)
                _transactionsList.value = transactionhistorymodel
            }catch (e: Exception){
                Log.e("API", "Error: ${e.message}")}
        }

    }



    private val _withdrawalData =MutableStateFlow<List<Withdrawaldtclss>?>(null)
    val withdrawalData:  StateFlow<List<Withdrawaldtclss>?> = _withdrawalData


    fun withdrawalrequest(){
        viewModelScope.launch {
            try {
                val response = Withdrawalobject.api.getwirhdrawalreq()
                _withdrawalData.value = response
            } catch (e: Exception) {
                _withdrawalData.value = null
            }
        }
    }


    private val alltransactionviemod = MutableStateFlow<List<alltransactiondc>?>(null)
    val alltransaction: StateFlow<List<alltransactiondc>?> = alltransactionviemod

    fun trasactionall(startDate: String? = null, endDate: String? = null) {
        viewModelScope.launch {
            try {
                val response = if (startDate != null && endDate != null) {
                    Log.d("TransactionVM", "Fetching with filter → start=$startDate , end=$endDate")
                    Withdrawalobject.api.alltransactioninterface(startDate, endDate)
                } else {
                    Log.d("TransactionVM", "Fetching all transactions (no filters)")
                    Withdrawalobject.api.alltransactioninterface()
                }

                Log.d("TransactionVM", "Response size = ${response.size}")
                response.forEach { tx ->
                    Log.d("TransactionVM", "Txn → id=${tx.requestid}, user=${tx.username}, coins=${tx.coins}")
                }

                alltransactionviemod.value = response
            } catch (e: Exception) {
                Log.e("TransactionVM", "Error fetching transactions", e)
                alltransactionviemod.value = null
            }
        }
    }



    fun approvewithdrawal(request: withdrawalresponse){
        viewModelScope.launch {
            try {
                val response = Withdrawalobject.api.approvewithdrawal(request)
                if (response.isSuccessful) {
                    Log.d("WithdrawalModelView", "Withdrawal approved successfully")
                } else {
                    Log.e("WithdrawalModelView", "Failed to approve withdrawal: ${response.code()}")
                }

    }catch (e: Exception){
                Log.e("WithdrawalModelView", "Error approving withdrawal: ${e.message}")
    }
    }
        }

}