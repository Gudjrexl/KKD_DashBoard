package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.R
import com.jrexl.novanectorclient.modelview.Kycmodelview
import com.jrexl.novanectorclient.objectapi.Urlobject

class kycdetailsindv :  ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactno = intent.getStringExtra("contactno") ?: ""
        val hide = intent.getStringExtra("hide") ?: ""
        setContent {
            Kycdetailscreen(contactno,hide)
        }
    }
}
@Composable
fun Kycdetailscreen(contactno: String,hide: String, vm: Kycmodelview = viewModel()){

    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)

val inddata by vm.kycDetail.collectAsState()
    LaunchedEffect(contactno) { vm.kycindivdetaildata(contactno) }
    val context = LocalContext.current
    val activity = context as? Activity

    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
        .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White))))
    {
        if (hide == "hide") {

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
                Text("Document", color = Color.Black, fontSize = 25.sp, modifier = Modifier.statusBarsPadding().padding(top = 12.dp, start = 16.dp))



            }



        }else{
            Text("Kyc verification", color = Color.Black, fontSize = 25.sp, modifier = Modifier.statusBarsPadding().padding(top = 12.dp, start = 16.dp).align(androidx.compose.ui.Alignment.CenterHorizontally))

        }
        Divider(color = Color.Gray, thickness = 0.8.dp, modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(13.dp))


        Text("Contact No: ${inddata?.contactno}", modifier = Modifier.padding(start = 16.dp, top = 10.dp))
        Spacer(modifier = Modifier.height(13.dp))

        Text("Aadhar No: ${inddata?.adharno}", modifier = Modifier.padding(start = 16.dp, top = 10.dp))
        Spacer(modifier = Modifier.height(13.dp))

        val adharUrl = inddata?.adharimg?.takeIf { it.isNotBlank() }?.let {
            "${Urlobject.BASE_URL.trimEnd('/')}/kycimagea/$it"
        }


        AsyncImage(
            ImageRequest.Builder(context)
                .data(adharUrl)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            modifier = Modifier.padding(16.dp).clip(RectangleShape).fillMaxWidth().height(300.dp)
        )
        Spacer(modifier = Modifier.height(13.dp))

        Text("Pan Card No: ${inddata?.Panno}", modifier = Modifier.padding(start = 16.dp, top = 10.dp))
        Spacer(modifier = Modifier.height(13.dp))

        val panUrl = inddata?.panimg?.takeIf { it.isNotBlank() }?.let {
            "${Urlobject.BASE_URL.trimEnd('/')}/kycimagep/$it"
        }

        AsyncImage(
            ImageRequest.Builder(context)
                .data(panUrl)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            modifier = Modifier.padding(16.dp).clip(RectangleShape).fillMaxWidth().height(300.dp)
        )





        Spacer(modifier = Modifier.padding(10.dp))
        val statusMessage by vm.kycStatusResult.collectAsState()

        statusMessage?.let {
            Text(text = it, color = Color.Black, modifier = Modifier.padding(16.dp))
        }
if (hide != "hide"){


        Row(modifier = Modifier.padding(16.dp)){
            Button(onClick = {
vm.kycstatus(contactno, "Rejected")


            }, modifier = Modifier.size(150.dp,40.dp),
                colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Sets the button background to black
                contentColor = Color.White    // Sets the text color to white
            )) { Text("Reject")}
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
vm.kycstatus(contactno, "Verified")
            },  modifier = Modifier.size(150.dp,40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )) { Text("Accept")}
        }}
        Spacer(modifier = Modifier.height(50.dp))


    }

}

