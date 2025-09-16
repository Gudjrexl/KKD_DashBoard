package com.jrexl.novanectorclient.modelview

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrexl.novanectorclient.dataclass.Productdetails
import com.jrexl.novanectorclient.objectapi.Productretro
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.jrexl.novanectorclient.dataclass.Productdetailsadd
import com.jrexl.novanectorclient.dataclass.categorydatclss
import com.jrexl.novanectorclient.dataclass.categorydatclsspost

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Productmodelview: ViewModel() {


    fun deleteCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                val response = Productretro.Productapi.deleteCategory(categoryName)

                    val body = response.body()

            } catch (e: Exception) {
print(e)            }
        }
    }



    fun deleteProduct(productName: String) {
        viewModelScope.launch {
            try {
                val response = Productretro.Productapi.deleteProduct(productName)

            } catch (e: Exception) {
print(e)            }
        }
    }


    var products by mutableStateOf<List<Productdetails>>(emptyList())
        private set


    private var isLoaded = false

    fun fetchproducr() {
        if (isLoaded) return

        viewModelScope.launch {
            try {
                products = Productretro.Productapi.GetProductDetl()
                isLoaded = true

            }
            catch (e: Exception){
                Log.e("ErrorFetching", e.toString())
            }

        } }



    private val _uploadResult = mutableStateOf("")
    val uploadResult: State<String> = _uploadResult

    fun uploadProduct(context: Context, data: Productdetailsadd, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                Log.d("ProductScreen", "Uploading product with data: $data")

                val resolver = context.contentResolver

                val imagePart = imageUri?.let { uri ->
                    val type = resolver.getType(uri) ?: "image/*"
                    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                    resolver.openInputStream(uri)?.use { input ->
                        tempFile.outputStream().use { output -> input.copyTo(output) }
                    }
                    val requestBody = tempFile.asRequestBody(type.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
                }
                Log.d("ProductScreen", "imagePart: $imagePart")

                val response = Productretro.Productapi.addProduct(
                    prodid = data.prodid.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    prodtname = data.Prodtname.toRequestBody("text/plain".toMediaTypeOrNull()),
                    description = data.Prodtdescription.toRequestBody("text/plain".toMediaTypeOrNull()),
                    price = data.Prodtprice.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    stockQuantity = data.ProdtstockQuantity.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    category = data.Prodtcategory.toRequestBody("text/plain".toMediaTypeOrNull()),
                    productCoin = data.ProductCoin.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    image = imagePart!!
                )
                Log.d("ProductScreen", "Response: $response")

                _uploadResult.value = if (response.isSuccessful) "✅ Success" else "❌ Error ${response.code()}"
                Log.d("ProductScreen", "result: ${_uploadResult.value}")


            } catch (e: Exception) {
                _uploadResult.value = "Exception: ${e.message}"
                Log.d("ProductScreen", "result catch: ${_uploadResult.value}")


            }
        }
    }







    var category by mutableStateOf<List<categorydatclss>>(emptyList())
        private set

    fun categoryviewmodel() {
        viewModelScope.launch {
            try {
                category = Productretro.Productapi.GetCategory()
            } catch (e: Exception) {
                Log.e("ErrorFetching", e.toString())
            }
        }
    }


    private val _uploadResultCategory = mutableStateOf("")
    val uploadResultCate: State<String> = _uploadResultCategory

    fun uploadCategory(context: Context, data: categorydatclsspost, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val contentResolver = context.contentResolver

                val namePart = data.name.toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart = imageUri?.let {
                    val inputStream = contentResolver.openInputStream(it)
                    val bytes = inputStream?.readBytes()
                    val reqBody = bytes!!.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData(
                        "ctimg",
                        "category_image.jpg",
                        reqBody
                    )
                }

                val response = Productretro.Productapi.AddCategory(namePart, imagePart)
                if (response.isSuccessful) {
                    Log.d("Upload", "✅ Category uploaded successfully")
                } else {
                    Log.e("Upload", "❌ Failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Upload", "⚠️ Exception: $e")
            }
        }
    }




}