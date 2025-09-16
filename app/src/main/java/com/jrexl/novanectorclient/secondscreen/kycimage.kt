package com.jrexl.novanectorclient.secondscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.R
import com.jrexl.novanectorclient.modelview.Kycmodelview
import com.jrexl.novanectorclient.objectapi.Urlobject

class kycimage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactno = intent.getStringExtra("contactno") ?: ""
        setContent {
            Kycdetailscreens(contactno)
        }
    }
}


@Composable
fun Kycdetailscreens(contactno: String, vm: Kycmodelview = viewModel()) {


    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)

    val inddata by vm.kycDetail.collectAsState()
    LaunchedEffect(contactno) { vm.kycindivdetaildata(contactno) }
    val context = LocalContext.current


    Column(modifier = Modifier.fillMaxWidth()
        .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White)))){
        Text("Adhar pan image", color = Color.Black, fontSize = 25.sp, modifier = Modifier.statusBarsPadding().padding(top = 12.dp, start = 16.dp).align(androidx.compose.ui.Alignment.CenterHorizontally))
        Divider(color = Color.Gray, thickness = 0.8.dp, modifier = Modifier.padding(vertical = 8.dp))


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
        Spacer(Modifier.height(10.dp))

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
    }


}
