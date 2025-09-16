package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.R
import com.jrexl.novanectorclient.dataclass.OfferDetDatacls
import com.jrexl.novanectorclient.modelview.Offerprmoviewmodel
import com.jrexl.novanectorclient.objectapi.Urlobject
import kotlin.collections.chunked

class Offer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            offerscrren()
        }
    }
}



@Composable
fun offerscrren(vm: Offerprmoviewmodel = viewModel()){
    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)

    val context = LocalContext.current
    val activity = context as? Activity
    var showaddofferscreen by remember { mutableStateOf(false) }
    if (showaddofferscreen) {
        AddPOgffercreen(onBack = {showaddofferscreen = false })
        return
    }

    LaunchedEffect(Unit) {
        vm.fetchofferproduct()
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
        .padding(start = 16.dp, end = 16.dp)
    ){
        Row(modifier = Modifier.statusBarsPadding().padding(top = 12.dp)){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        activity?.finish()
                    }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "offer", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    showaddofferscreen = true

                },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
        }
Spacer(Modifier.height(15.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(1.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                val productList = vm.offerprod
                items(productList.chunked(2)) { ritm ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (product in ritm) {
                            Box(modifier = Modifier.weight(1f)) {
                                Offercard(product)
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
fun AddPOgffercreen(onBack: () -> Unit, vm: Offerprmoviewmodel = viewModel()) {

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var desc by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val uploadState by vm.uploadStates.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )
    val context = LocalContext.current
    val activity = context as? Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White)))
            .padding(start = 16.dp, end= 16.dp)
    )
    {

        Row(modifier = Modifier.statusBarsPadding().padding(top = 12.dp)){

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        activity?.finish()
                    }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Add offer", fontSize = 20.sp, color = Color.Black)

        }
Spacer(Modifier.height(15.dp))


        Text(text = "Product Name",
//            modifier = Modifier
//            .padding(start = 10.dp),
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ))
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Product Name", fontSize = 11.sp,  modifier = Modifier.offset(y= -4.dp)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Product Description",
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ))
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            placeholder = { Text("About Product ", fontSize = 11.sp,  modifier = Modifier.offset(y= -4.dp)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Purchased Coin",
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ))
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            placeholder = { Text("Purchased Coin", fontSize = 11.sp,  modifier = Modifier.offset(y= -4.dp)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
                .clip(RoundedCornerShape(10.dp))
                .clickable { galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected Offer Image",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Tap to select offer image", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var showbutton by remember { mutableStateOf(true) }
if (showbutton){
    Button(
        onClick = {
            if (name.text.isNotEmpty() && desc.text.isNotEmpty() && price.text.isNotEmpty()) {
                vm.addOffer(name.text, desc.text, price.text.toInt(), selectedImageUri, context)
            }
            showbutton = false
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Text("Upload Offer")
    }
}


        Spacer(modifier = Modifier.height(16.dp))

        if (uploadState.isNotEmpty()) {
            Text(uploadState, color = Color.Black)
        }
    }
}

@Composable
fun Offercard(product: OfferDetDatacls, vm: Offerprmoviewmodel = viewModel()) {
    val hardcodedImageUrl = (Urlobject.BASE_URL + product.Offerimg) ?: ""
    var dltoption by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp, bottom = 2.dp)
                    .fillMaxWidth()
                    .height(170.dp)
                    .clickable(onClick = {
                        dltoption = !dltoption
                    })
            ) {
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
                        .clip(RoundedCornerShape(10.dp))

                )

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
vm.deleteOffer(product.offername)
                                Toast.makeText(context, "offer deleted Deleted, Please refresh", Toast.LENGTH_SHORT).show()
                                dltoption = false
                            })
                        )

                    }}




            }

            androidx.compose.material3.Text(
                text = product.offername,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
//            Row ( modifier = Modifier.padding(5.dp)
//,                    verticalAlignment = Alignment.CenterVertically ){
//                Icon(
//                    Icons.Filled.MonetizationOn,
//                    "dollar",
//                    tint = Color(0xFFFFD700),
//                )
//                androidx.compose.material3.Text(
//                text = product.offerprice.toString(),
//                style = MaterialTheme.typography.titleMedium,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.padding(4.dp)
//            ) }

            Spacer(modifier = Modifier.height(2.dp))

        }
    }

}