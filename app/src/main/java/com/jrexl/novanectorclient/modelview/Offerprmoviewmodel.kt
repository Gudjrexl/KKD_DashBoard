package com.jrexl.novanectorclient.modelview

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrexl.novanectorclient.dataclass.OfferDetDatacls
import com.jrexl.novanectorclient.dataclass.Offerpromodtclss
import com.jrexl.novanectorclient.objectapi.HomePageRetro
import com.jrexl.novanectorclient.objectapi.Offerpromoretro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Offerprmoviewmodel: ViewModel() {

    fun deleteHomeImage(iconName: String) {
        viewModelScope.launch {
            try {
                val response = Offerpromoretro.offerapi.deleteHomeImage(iconName)

            } catch (e: Exception) {
                print(e)            }
        }
    }

    fun deleteOffer(offerName: String) {
        viewModelScope.launch {
            try {
                val response = Offerpromoretro.offerapi.deleteOffer(offerName)

            } catch (e: Exception) {
print(e)            }
        }
    }



    private val _uploadStates = MutableStateFlow<String>("")
    val uploadStates: StateFlow<String> get() = _uploadStates

    fun addOffer(
        name: String,
        desc: String,
        price: Int,
        imageUri: Uri?,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
                val descBody = RequestBody.create("text/plain".toMediaTypeOrNull(), desc)
                val priceBody = RequestBody.create("text/plain".toMediaTypeOrNull(), price.toString())

                val imagePart = imageUri?.let {
                    val file = uriToFiles(it, context)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("oimage", file.name, requestFile)
                }

                val response = Offerpromoretro.offerapi.addOffer(nameBody, descBody, priceBody, imagePart)
                if (response.isSuccessful) {
                    _uploadStates.value = response.body()?.message ?: "Offer uploaded!"
                } else {
                    _uploadStates.value = "Upload failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _uploadStates.value = "Error: ${e.message}"
            }
        }
    }

    private fun uriToFiles(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "offer_image_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { output ->
            inputStream?.copyTo(output)
        }
        return file
    }

    private val _promopic = MutableStateFlow<List<Offerpromodtclss>>(emptyList())
    val promopic: StateFlow<List<Offerpromodtclss>> = _promopic
    fun promopicget(){
        viewModelScope.launch {
            try {
                val response = Offerpromoretro.offerapi.promo()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _promopic.value = it
                    } ?: Log.e("Offerprmoviewmodel", "Empty body")
                } else {
                    Log.e("Offerprmoviewmodel", "Failed: ${response.code()} ${response.message()}")
                }
            }catch (e: Exception){
                Log.e("Error", e.toString())
            }
        }
    }


    private val _uploadState = MutableStateFlow<String>("")
    val uploadState: StateFlow<String> get() = _uploadState

    fun uploadImage(fileUri: Uri, context: android.content.Context) {
        viewModelScope.launch {
            try {
                val file = uriToFile(fileUri, context)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val response = Offerpromoretro.offerapi.uploadHomeImage(body)
                if (response.isSuccessful) {
                } else {
                    _uploadState.value = "Upload failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _uploadState.value = "Error: ${e.message}"
            }
        }


}

    private fun uriToFile(uri: Uri, context: android.content.Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "upload_image.jpg")
        file.outputStream().use { inputStream?.copyTo(it) }
        return file
    }



    var offerprod by mutableStateOf<List<OfferDetDatacls>>(emptyList())
        private set


    private var isofferLoaded = false

    fun fetchofferproduct() {
        if (isofferLoaded) return

        viewModelScope.launch {
            try {
                offerprod = Offerpromoretro.offerapi.GetofferDetl()
                isofferLoaded = true

            }
            catch (e: Exception){
                Log.e("ErrorFetching", e.toString())
            }

        } }
}
