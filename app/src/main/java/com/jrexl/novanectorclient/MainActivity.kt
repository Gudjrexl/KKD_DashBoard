package com.jrexl.novanectorclient

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jrexl.novanectorclient.objectapi.Datamanger
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
Spashpage()
        }
    }
}

@Composable
fun Spashpage(){
    val activity = LocalActivity.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val clid = Datamanger.getClienrid(context)
        delay(3000L)
        if (!clid.isNullOrEmpty()) {
            activity?.startActivity(Intent(activity, Homepage::class.java))
        } else {
            activity?.startActivity(Intent(activity, Login::class.java))
        }
        activity?.finish()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFADD8E6), Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.splash),
            contentDescription = "splashpage",
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
        Spashpage( )

}