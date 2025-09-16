package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.modelview.HomePageviewmodel
import com.jrexl.novanectorclient.modelview.Offerprmoviewmodel
import com.jrexl.novanectorclient.objectapi.Urlobject
import java.lang.String

class OfferPromotion :ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfferScreen()
        }
    }
}
@Composable
fun  OfferScreen(vm: Offerprmoviewmodel = viewModel()){

    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)

    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        vm.promopicget()
    }
   var showaddscreen by remember { mutableStateOf(false) }
    if (showaddscreen) {
        AddPOffercreen(onBack = { showaddscreen = false })
        return
    }
    val promopic by vm.promopic.collectAsState()
    Column(modifier = Modifier.fillMaxSize()
        .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White)))
        .padding(start = 16.dp, end = 16.dp)){
        Row (modifier = Modifier.statusBarsPadding().padding(top = 12.dp)){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        activity?.finish()
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Promotion", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
showaddscreen = true

                },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
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
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(Modifier.padding(bottom = 8.dp).fillMaxHeight().fillMaxWidth()) {

            items(promopic.size) { index ->
                val promotion = promopic[index]
                val hardcodedImageUrl = (Urlobject.BASE_URL + "/uploads/" + promotion.img) ?: ""
                var dltoption by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clickable(onClick = {
                            dltoption = !dltoption

                        })

                ){
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(hardcodedImageUrl)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "promotion Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    if (dltoption){
                        val dltstr = promotion.img ?: ""
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
                                    vm.deleteHomeImage(dltstr)

                                    Toast.makeText(context, "offer deleted Deleted, Please refresh", Toast.LENGTH_SHORT).show()
                                    dltoption = false
                                })
                            )

                        }}
                }

                Spacer(Modifier.height(15.dp))

            }

        }

        Spacer(Modifier.height(25.dp))


    }



}

@Composable
fun AddPOffercreen(onBack: () -> Unit, vm: Offerprmoviewmodel = viewModel()) {
    LaunchedEffect(Unit) {
    }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val uploadState by vm.uploadState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )
    val context = LocalContext.current
    val activity = context as? Activity
    


    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White))
        )
        .padding(16.dp)
    ) {


        Row (modifier = Modifier.statusBarsPadding().padding(top = 1.dp)){
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
            Text(text = "Add Banner", fontSize = 20.sp, color = Color.Black)



        }

Spacer(Modifier.height(20.dp))

        Text("Banner Image", color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(1.dp))
                .background(Color.White)
                .clickable { galleryLauncher.launch("image/*") }
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ){
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected Banner",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Tap to select banner", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Upload button
        if (selectedImageUri != null) {
            Row {

                Button(
                    onClick = { selectedImageUri = null},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )

                ) {
                    Text("Clean")
                }
                
                Spacer(Modifier.width(15.dp))

                Button(
                    onClick = { vm.uploadImage(selectedImageUri!!, context)
                        onBack()},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )

                ) {
                    Text("Upload Banner")
                }
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        if (uploadState.isNotEmpty()) {
            Text(uploadState, color = Color.Black, modifier = Modifier.padding(10.dp))
        }
    }

}


@Composable
@Preview(showBackground = true)
fun OfferScreenPreview(){
    OfferScreen()
}