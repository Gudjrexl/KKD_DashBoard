package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.modelview.UserprofileViewmodel
import com.jrexl.novanectorclient.objectapi.Urlobject

class UserDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UseScren()
        }
    }
}
@Composable
fun UseScren(vm: UserprofileViewmodel = viewModel()){
    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    val context = LocalContext.current

    val activity = context as? Activity
    LaunchedEffect(Unit){
        vm.loadUserData()
    }

    val userprofile by vm.profiledata.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFADD8E6),
                        Color.White
                    )
                )
            )
    )
    {
        Row(modifier = Modifier.statusBarsPadding().padding(start=16.dp, top = 12.dp)){

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
                Text(text = "Registered User", fontSize = 20.sp, color = Color.Black)

        }

        LazyColumn(modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)) {
            userprofile?.let { profiles ->
                items(profiles.size) { index ->
                    val profile = profiles[index]
                    Userdetails(
                        primg = profile.imageurl ?: "image",
                        username = profile.Username ?: "name",
                        contact = profile.contactno ?: "phone",
                        onClick = {
                            val intent3 = Intent(context, com.jrexl.novanectorclient.secondscreen.Completeprofile::class.java).apply {
                                putExtra("contactno", profile.contactno ?: "phone")
                            }
                            context.startActivity(intent3)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }


    }

}

@Composable
fun Userdetails(primg: String, username: String, contact: String, onClick: () -> Unit) {

    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)

    val imageUrl = Urlobject.BASE_URL + "/profilepic/" + primg

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .wrapContentHeight()
            .clickable { onClick() }
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = username,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = contact,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                "View Details",
                color = Color.Blue,
                fontSize = 14.sp
            )
            Spacer(Modifier.width(6.dp))
        }
    }


}

@Composable
@Preview(showBackground = true)
fun UseScrenPreview(){
    UseScren()
}