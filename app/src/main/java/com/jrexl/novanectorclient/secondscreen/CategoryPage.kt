package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.dataclass.categorydatclss
import com.jrexl.novanectorclient.dataclass.categorydatclsspost
import com.jrexl.novanectorclient.modelview.Productmodelview
import com.jrexl.novanectorclient.objectapi.Urlobject
import kotlin.collections.chunked

class CategoryPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            categoryScreen()
        }
    }
}

@Composable

fun categoryScreen(vm: Productmodelview = viewModel()) {

    @Suppress("DEPRECATION") val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    val context = LocalContext.current
    val activity = context as? Activity
    var showAddScreen by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        vm.categoryviewmodel()
    }


    if (showAddScreen) {
        AddCategoryScreen(onBack = {
            showAddScreen = false
            vm.categoryviewmodel()
        })
        return
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
    ){
        Row(modifier = Modifier.statusBarsPadding().padding(start = 16.dp, end = 16.dp,top = 12.dp)){

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
            Text(text = "Category", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {

                    showAddScreen = true

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
                    contentDescription = "add",
                    tint = Color.Black
                )
            }


        }
        Spacer(Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 7.dp),
            contentPadding = PaddingValues(1.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            val productList = vm.category
            items(productList.chunked(3)) { ritm ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (product in ritm) {
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryCardCard(product)
                        }
                    }
                    repeat(3 - ritm.size) {
                        Box(modifier = Modifier.weight(1f)) { /* Empty space */ }
                    }
                }
            }
        }

    }


}

@Composable
fun AddCategoryScreen(onBack: () -> Unit, vm: Productmodelview = viewModel()) {
    @Suppress("DEPRECATION") val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    var Category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White)))

           ){
        Row(modifier = Modifier.statusBarsPadding().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text("Create New Category", fontSize = 22.sp,color = Color.Black, fontWeight = FontWeight.Medium)

        }




        Spacer(modifier = Modifier.height(15.dp))

        androidx.compose.material3.Text(
            text = "Category Name", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 18.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.OutlinedTextField(
            value = Category?: "",
            onValueChange = { Category = it},
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
            text = "Category Image", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 18.dp),
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
                .padding(10.dp),
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
        Spacer(modifier = Modifier.height(20.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)

                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
                .clickable {     
                    vm.uploadCategory(
                        context,
                        categorydatclsspost(name = Category),
                        imageUri
                    )
                    vm.categoryviewmodel()
                    onBack() // move this inside ViewModel only after success
                }



                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Upload", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }

}

@Composable
fun CategoryCardCard(product: categorydatclss, vm: Productmodelview = viewModel()) {

    var dltoption by remember { mutableStateOf(false) }

   val hardcodedImageUrl = Urlobject.BASE_URL + product.ctimg



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = {
                dltoption = !dltoption
            })
    ) {
        Column {
            val context = LocalContext.current

            // Image
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(hardcodedImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            if (dltoption){
                Row(modifier = Modifier
                    .padding(4.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 3.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Coin Icon",
                        tint = Color(0xFF0A0A0A),
                        modifier = Modifier.fillMaxWidth().height(30.dp).clickable(onClick = {

vm.deleteCategory(product.name)
                            Toast.makeText(context, "offer deleted Deleted, Please refresh", Toast.LENGTH_SHORT).show()
                            dltoption = false
                        })
                    )

                }}
            Spacer(Modifier.height(2.dp))
            Row(  verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    color = Color.Black,
                )
                Spacer(Modifier.weight(1f))

            }


        }
    }

}

@Composable
@Preview(showBackground = true)
fun categoryScreenPreview() {
    categoryScreen()
}
