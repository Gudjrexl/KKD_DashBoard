package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.dataclass.withdrawalresponse
import com.jrexl.novanectorclient.modelview.UserprofileViewmodel
import com.jrexl.novanectorclient.modelview.WithdrawalModelView
import com.jrexl.novanectorclient.objectapi.Urlobject

class WithdrawalPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Withdrawalscreen()
        }
    }
}
@Composable
fun Withdrawalscreen(vm: WithdrawalModelView = viewModel()) {
    @Suppress("DEPRECATION") val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
vm.withdrawalrequest()

    }
val withdrawalrequests by vm.withdrawalData.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White)))) {
        Row(modifier = Modifier.statusBarsPadding().padding(start = 16.dp, top = 12.dp)) {
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
            Text(text = "Withdrawal Request", fontSize = 20.sp, color = Color.Black)


        }
        LazyColumn(modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 7.dp)) {

            withdrawalrequests?.let { profile ->
                items(profile.size) { index ->
                    val profile = profile[index]
                    val primgValue = profile.profilepic ?: "image"
                    Log.d("WithdrawalCardDebug", "primg = $primgValue")
Withdrawalcard(
    primg = primgValue?: "image",
    username = profile.coinusername ?: "name",
    contact = profile.contactcno ?: "phone",
    coin = profile.coins,
    requestid = profile.requestid
    )

                }


            }
        }
    }}

@Composable
fun Withdrawalcard(
    primg: String,
    username: String,
    contact: String,
    coin: Number,
    requestid: String, vm: WithdrawalModelView = viewModel()
) {
var reqvis by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }
    var reqno by remember { mutableStateOf(0) }

    if (isVisible) {



        val imageUrl = if (primg.isNotEmpty()) {
            Urlobject.BASE_URL + "/profilepic/" + primg
        } else {
            null
        }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .wrapContentHeight()
            .background(color = Color.White)
    ) {

        Column {
            Row(
                modifier = Modifier.padding(8.dp).clickable(onClick = {
                    if (reqno % 2 == 0 ){
                        reqvis = true
                        reqno++

                    }
                    else{
                        reqvis = false
                        reqno++

                    }
                }),
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
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f).clickable(onClick = {
                    if (reqno % 2 == 0 ){
                        reqvis = true
                        reqno++
                    }
                    else{
                        reqvis = false
                        reqno++


                    }
                })) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.MonetizationOn,
                        "dollar",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        coin.toString(),
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }

            }

            if (reqvis) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            ) {

                OutlinedButton(

                    onClick = {
                        val request = withdrawalresponse(
                            requestid,
                            contact,
                            coin.toInt(),
                            status = "rejected"
                        )
                        vm.approvewithdrawal(request)
                        isVisible = false
                    },
                    Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.Black),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent, // Transparent background
                        contentColor = Color.Black
                    )
                ) {
                    Text("Reject")
                }
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedButton(
                    onClick = {
                        val request = withdrawalresponse(
                            requestid,
                            contact,
                            coin.toInt(),
                            status = "approved"
                        )
                        vm.approvewithdrawal(request)
                        isVisible = false
                    },
                    Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.Black),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Accept")
                }
            }
                Spacer(Modifier.padding(bottom = 14.dp))
        }
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}
}

@Composable
@Preview(showBackground = true)
fun WithdrawalscreenPreview() {
    Withdrawalscreen()
}