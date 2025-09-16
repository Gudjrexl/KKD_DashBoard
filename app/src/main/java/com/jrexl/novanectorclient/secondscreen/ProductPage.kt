package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.jrexl.novanectorclient.dataclass.Productdetailsadd
import com.jrexl.novanectorclient.modelview.HomePageviewmodel
import com.jrexl.novanectorclient.modelview.Productmodelview
import com.jrexl.novanectorclient.objectapi.Urlobject
import kotlin.math.log

class ProductPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductScreen()
        }
    }
}

@Composable
fun ProductScreen(vm: Productmodelview = viewModel()) {
    @Suppress("DEPRECATION") val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
val context = LocalContext.current
    val activity = context as? Activity
    var showAddScreen by remember { mutableStateOf(false) }
    if (showAddScreen) {
        AddProductScreen(onBack = { showAddScreen = false })
        return
    }


    LaunchedEffect(Unit){
        vm.fetchproducr()
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFB3E5FC),
                    Color.White
                )
            )
        )
        .padding(16.dp)
    ){
        Row(modifier = Modifier.statusBarsPadding().padding(end = 16.dp)){

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        activity?.finish()
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Product", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {

                    showAddScreen = true

                },
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Black
                )
            }


        }

        Spacer(modifier = Modifier.size(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            val productList = vm.products
            items(productList.chunked(2)) { ritm ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Actual products
                    for (product in ritm) {
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(product)
                        }
                    }
                    repeat(2 - ritm.size) {
                        Box(modifier = Modifier.weight(1f)) { }
                    }
                }
            }
        }




    }

}

@Composable
fun AddProductScreen(onBack: () -> Unit, vm: Productmodelview = viewModel()) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var coin by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var quan by remember { mutableStateOf("") }
    var qrCode by remember { mutableStateOf("") }
val context = LocalContext.current
    var Productcatogory by remember { mutableStateOf("") }
    var productid by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    LaunchedEffect(Unit) {
        vm.categoryviewmodel()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White))
            )   ) {
        Row(modifier = Modifier.statusBarsPadding().padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add New Product", fontSize = 22.sp,color = Color.Black, fontWeight = FontWeight.Medium)

        }

        Spacer(modifier = Modifier.height(15.dp))






        androidx.compose.material3.Text(
            text = "Product ID", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.OutlinedTextField(
            value = productid?: "",
            onValueChange = { productid = it},
            placeholder = {
                Text(
                    "Product ID",
                    fontSize = 11.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                .height(48.dp),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(15.dp))

        androidx.compose.material3.Text(
            text = "Product Name", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.OutlinedTextField(
            value = name?: "",
            onValueChange = { name = it},
            placeholder = {
                Text(
                    "Product Name",
                    fontSize = 11.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                .height(48.dp),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(15.dp))

        androidx.compose.material3.Text(
            text = "Product Coin", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.OutlinedTextField(
            value = coin?: "",
            onValueChange = { coin = it},
            placeholder = {
                Text(
                    "Product Coin",
                    fontSize = 11.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                .height(48.dp),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(15.dp))


        androidx.compose.material3.Text(
            text = "Product Price", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.OutlinedTextField(
            value = price?: "",
            onValueChange = { price= it},
            placeholder = {
                Text(
                    "Product Price",
                    fontSize = 11.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                .height(48.dp),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )






        Spacer(modifier = Modifier.height(15.dp))






        androidx.compose.material3.Text(
            text = "Product Category", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))

        val categoryOptions = vm.category
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .background(Color.White)
                .border(1.dp, Color.Black )
                .clickable { expanded = true }
                .height(48.dp)
            ,

        ) {
            Text(
                text = if (Productcatogory.isNotEmpty()) Productcatogory else "Select Category", fontSize = 11.sp,
                color = if (Productcatogory.isNotEmpty()) Color.Black else Color.Gray,
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 15.dp) // 🔹 center vertically, padding start

            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp).width(250.dp)
            ) {
                categoryOptions.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            Productcatogory = category.name
                            expanded = false
                        },
                    )
                }
            }





        }

        Spacer(modifier = Modifier.height(15.dp))


        androidx.compose.material3.Text(
            text = "Product Description", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.OutlinedTextField(
            value = desc?: "",
            onValueChange = {desc = it},
            placeholder = {
                Text(
                    "Product Description",
                    fontSize = 11.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                .height(48.dp),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(15.dp))









        androidx.compose.material3.Text(
            text = "Product Image", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .height(100.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(1.dp))
                .background(Color.White)
                .clickable { galleryLauncher.launch("image/*") }
               ,
            contentAlignment = Alignment.Center
        ){
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else {
                Text("Tap to select image from gallery", color = Color.Black)
            }
        }
        val randomTwoDigit = remember { (10..999999).random() }
        Spacer(modifier = Modifier.height(12.dp))
        if (coin.isNotEmpty() && desc.isNotEmpty() && name.isNotEmpty() && price.isNotEmpty()){
           qrCode = "nova$randomTwoDigit$productid"
            val json = remember {
                """
{
"coins": $coin,
 "scanQRCodes" :"$qrCode"}
        """.trimIndent()
            }
            Text("Qr genrate", color = Color.Black, modifier = Modifier.padding(start = 10.dp))
            val qrBitmap: Bitmap = remember(json) { generateQRBitmap(json) }
            Spacer(modifier = Modifier.height(4.dp))
            Image(bitmap = qrBitmap.asImageBitmap(), contentDescription = "QR Code", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(7.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))

                Text(qrCode.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(start = 10.dp))
                Spacer(modifier = Modifier.weight(1f))

            }

        }
        Spacer(modifier = Modifier.height(7.dp))



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)

                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
                .clickable {
                    val addproddata = Productdetailsadd(
                        prodid = productid.toIntOrNull() ?: 0, // ✅ safe
                        Prodtname = name,
                        Prodtdescription = desc,
                        Prodtprice = price.toIntOrNull() ?: 1, // ✅ safe
                        ProdtstockQuantity = quan.toIntOrNull() ?: 1, // ✅ safe
                        Prodtcategory = Productcatogory,
                        ProductCoin = coin.toIntOrNull() ?: 0 // ✅ safe,
                        , qr = qrCode
                    )

                    vm.uploadProduct(context, addproddata, imageUri)
                    Log.d("ProductScreen", "Uploaded invoked ✅ $addproddata")

                    Log.d("ProductScreen", "QR Code: $qrCode")

                    onBack()
                }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Submit",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(200.dp))






    }
}

private fun generateQRBitmap(data: String, size: Int = 512): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(
        data,
        BarcodeFormat.QR_CODE,
        size,
        size
    )

    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }

    return bmp
}


@Composable
fun ProductCard(product: com.jrexl.novanectorclient.dataclass.Productdetails, vm: Productmodelview = viewModel()) {
    val hardcodedImageUrl = (Urlobject.BASE_URL + product.Productimg) ?: ""
    var dltoption by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(7.dp)
        )  {
            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clickable(onClick = {
                        dltoption = !dltoption

                    })
            )  {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(hardcodedImageUrl)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 3.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coin Icon",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.ProductCoin}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                }
                if (dltoption){
                Row(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(4.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 3.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Coin Icon",
                        tint = Color(0xFF0A0A0A),
                        modifier = Modifier.fillMaxWidth().height(30.dp).clickable(onClick = {

                            vm.deleteProduct(product.Prodtname)
                            Toast.makeText(context, "Product Deleted, Please refresh", Toast.LENGTH_SHORT).show()
                            dltoption = false
                        })
                    )

                }}
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.Prodtname,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${product.qr}",

                )
        }
    }


}

@Composable
@Preview(showBackground = true)
fun ProductScreenPreview() {
    ProductScreen()
}

