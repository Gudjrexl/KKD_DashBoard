package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.R
import com.jrexl.novanectorclient.modelview.Kycmodelview

class Kycreq : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Kycscreen()
        }
    }
}
@Composable
fun Kycscreen(vm: Kycmodelview = viewModel()) {
    @Suppress("DEPRECATION") val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        vm.kycprofileget()

    }
    val kycdata by vm.kycdata.collectAsState()

    Column(modifier = Modifier.fillMaxSize()
        .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White)))
        )
    {
        Row(modifier = Modifier.statusBarsPadding().padding(top = 12.dp, start = 16.dp, end = 16.dp)){
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
            Text(text = "Kyc Request", fontSize = 20.sp, color = Color.Black)

        }


        LazyColumn(modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)) {
kycdata?.let { profile ->
    items(profile.size){
            index ->
        val profile = profile[index]
        Userdetails(
            primg = profile.imageurl ?: "image",
            username = profile.Username ?: "name",
            contact = profile.contactno ?: "phone",
            onClick = {
                val intent3 = Intent(context, kycdetailsindv::class.java).apply {
                    putExtra("contactno", profile.contactno ?: "phone")
                }
                context.startActivity(intent3)
            }
        )

    }
}

        }
    }

}

@Composable
@Preview(showBackground = true)
fun KycscreenPreview() {
    Kycscreen()

}