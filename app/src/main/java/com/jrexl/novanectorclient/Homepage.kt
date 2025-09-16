@file:Suppress("DEPRECATION")

package com.jrexl.novanectorclient

import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import android.graphics.Color as AndroidColor
import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.os.Environment
import android.widget.Button
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.dataclass.categorydatclsspost
import com.jrexl.novanectorclient.dataclass.qrcheckdb
import com.jrexl.novanectorclient.dataclass.withdrawalresponse
import com.jrexl.novanectorclient.modelview.HomePageviewmodel
import com.jrexl.novanectorclient.objectapi.HomePageRetro
import com.jrexl.novanectorclient.secondscreen.CategoryPage
import com.jrexl.novanectorclient.secondscreen.Kycreq
import com.jrexl.novanectorclient.secondscreen.Offer
import com.jrexl.novanectorclient.secondscreen.OfferPromotion
import com.jrexl.novanectorclient.secondscreen.ProductPage
import com.jrexl.novanectorclient.secondscreen.Transaction
import com.jrexl.novanectorclient.secondscreen.UserDetails
import com.jrexl.novanectorclient.secondscreen.WithdrawalPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Homepage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                AndroidColor.BLACK,   // bar color
                AndroidColor.BLACK    // scrim for dark mode
            ),
            navigationBarStyle = SystemBarStyle.light(
                AndroidColor.BLACK,
                AndroidColor.BLACK
            )
        )

        setContent {

            HomeScreen()
        }
    }
}
@Composable
fun HomeScreen(vm: HomePageviewmodel = viewModel()) {
    val systemUiController = rememberSystemUiController()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Status bar styling
    DisposableEffect(Unit) {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
        onDispose { systemUiController.setStatusBarColor(Color.Unspecified, darkIcons = false) }
    }

    // Refresh when coming back
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.loadUserData()
                vm.hometransaction()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val context = LocalContext.current
    val homedata by vm.userData.collectAsState()
    val hometrans by vm.transdata.collectAsState()
    var showtrans by remember { mutableStateOf(false) }
    var showexplore by remember { mutableStateOf(false) }

    if (showexplore){
        explorefun(onDismiss = { showexplore = false },
            onConfirm = {
                showexplore = false
            })
    }
    LaunchedEffect(Unit) {
        vm.loadUserData()
        vm.hometransaction()
       }




    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFADD8E6), Color.White)))){
        Row(modifier = Modifier.statusBarsPadding().padding(start=16.dp, top = 12.dp)) {
            Text("Welcome back,", color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold )
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                showexplore = true

            },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    Color.White
                ),
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp, start = 16.dp).size(40.dp), contentPadding = PaddingValues(start = 13.dp, end = 13.dp, ) )
            {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(44.dp)) {
                    Text(
                        "Explore All",
                        fontSize = 15.sp,
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Right Arrow",
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .size(15.dp)
                    )
                }
            }

        }
        Text("Admin!", color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold , modifier = Modifier.padding(start=16.dp, top = 2.dp))

        LazyColumn(modifier = Modifier.padding(top = 10.dp, start = 20.dp,end = 20.dp)) {
            item {
                homedata?.let { data ->
                    Cardview(title = "TOTAL USER", value = data.totalUser, onClick = {
                        Toast.makeText(context, "Navigating...", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(context, UserDetails::class.java)
                        context.startActivity(intent2)
                    })
                    Spacer(modifier = Modifier.height(15.dp))
                    Cardview(title = "TOTAL PRODUCT", value = data.totalProduct, onClick = {
                        Toast.makeText(context, "Navigating...", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(context, ProductPage::class.java)
                        context.startActivity(intent2)

                    })
                    Spacer(modifier = Modifier.height(15.dp))
                    Cardview(title = "KYC REQUEST", value = data.kycRequest, onClick = {
val intent2 = Intent(context, Kycreq::class.java)
                        context.startActivity(intent2)
                    })
                    Spacer(modifier = Modifier.height(15.dp))
                    Cardview(title = "WITHDRAWAL REQUEST", value = data.withdrawalRequest, onClick = {
                        Toast.makeText(context, "Navigating...", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(context, WithdrawalPage::class.java)
                        context.startActivity(intent2)
                    })
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
//item {
//
//    Row {     Text("Transaction Details")
//        Spacer(modifier = Modifier.width(100.dp))
//        Text("View details", color = Color.Blue, modifier = Modifier.clickable(onClick = {showtrans = true}) )
//
//    }
//    Spacer(modifier = Modifier.height(50.dp))
//
//    Row {            val coroutineScope = rememberCoroutineScope()
//        var isDownloading by remember { mutableStateOf(false) }
//
//        Text(
//            text = if (isDownloading) "Downloading..." else "Download Transaction",
//            color = if (isDownloading) Color.Gray else Color.Blue,
//            modifier = Modifier.clickable(enabled = !isDownloading) {
//                isDownloading = true
//                coroutineScope.launch {
//                    val success = downloadTransactionCsv(context)
//                    isDownloading = false
//                    Toast.makeText(
//                        context,
//                        if (success) "Transaction CSV saved to Downloads folder" else "Download failed",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.width(100.dp))
//
//
//
//
//
//    }
//if (showtrans){
//
//
//    val scrollState = rememberScrollState()
//
//    Box(
//        modifier = Modifier
//            .horizontalScroll(scrollState)
//            .fillMaxWidth()
//    ) {
//        Column {
//        Row(modifier = Modifier.fillMaxWidth().padding(start = 3.dp)) {
//            Text("UserId", color = Color.Black, fontSize = 12.sp, modifier = Modifier.padding(8.dp).width(120.dp))
//
//            Text(
//                "User Name",
//                color = Color.Black,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(8.dp).width(120.dp)
//            )
//            Text(
//                "Contact Detail",
//                color = Color.Black,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(8.dp).width(120.dp)
//            )
//
//            Text(
//                "Coupon ID",
//                color = Color.Black,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(8.dp).width(120.dp)
//            )
//
//            Text(
//                "Coupon Name",
//                color = Color.Black,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(8.dp).width(120.dp)
//            )
//
//            Text(
//                "Date & Time",
//                color = Color.Black,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(8.dp).width(120.dp)
//            )
//            Text("Coins", color = Color.Black, fontSize = 12.sp, modifier = Modifier.padding(8.dp).width(120.dp))
//        }
//        Spacer(modifier = Modifier.height(15.dp))
//        hometrans.forEach { data ->
//            Row(
//                modifier = Modifier
//                    .padding(start = 3.dp)
//            ) {
//                Text(data.userid ?: "NA", modifier = Modifier.padding(8.dp).width(120.dp))
//
//                Text(data.username ?: "NA", modifier = Modifier.padding(8.dp).width(120.dp))
//                Text(data.contactcno ?: "NA", modifier = Modifier.padding(8.dp).width(120.dp))
//
//                Text(data.itemexp ?: "NA", modifier = Modifier.padding(8.dp).width(120.dp))
//
//                Text(data.typetransaction ?: "NA", modifier = Modifier.padding(8.dp).width(120.dp))
//
//                Text(data.date, modifier = Modifier.padding(8.dp).width(100.dp))
//                Text(data.coins.toString() ?: "NA", modifier = Modifier.padding(8.dp).width(120.dp))
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//
//    }
//}}
//}

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun explorefun(onDismiss: () -> Unit,
               onConfirm: () -> Unit) {

    var showcheckqr by remember { mutableStateOf(false) }





    val context = LocalContext.current
    Dialog(onDismissRequest = {onDismiss()},
        properties = DialogProperties(
        decorFitsSystemWindows = false
    )
    ) {

        Card(
            modifier = Modifier.wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                Color.White)
        ) {

            Column(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Explore All", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF0FB))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF333333),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(17.dp))

                Box{
                    Column {
                    Row(Modifier.border(width = 1.dp, Color(0xFF333333),
                        RoundedCornerShape(10.dp)).padding(start = 12.dp, 6.dp, bottom = 6.dp, end = 12.dp).height(35.dp)
                        .clickable(onClick = {
                        val intent2 = Intent(context, CategoryPage::class.java)
                        context.startActivity(intent2) }),

                        verticalAlignment = Alignment.CenterVertically)
                    {
                        Text("Category",
                            color = Color(0xFF333333),
                            fontSize = 12.sp, fontWeight = FontWeight.Normal)
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            "angelbracket",
                            tint = Color(0xFF333333),
                            modifier = Modifier.size(13.dp)
                        )

                    }

                        Spacer(Modifier.height(15.dp))


                        Row(Modifier.border(width = 1.dp, Color(0xFF333333),
                            RoundedCornerShape(10.dp)).padding(start = 12.dp, 6.dp, bottom = 6.dp, end = 12.dp).height(35.dp)
                            .clickable(onClick = {

                                showcheckqr = true

                                 }),

                            verticalAlignment = Alignment.CenterVertically)
                        {
                            Text("Check Qr",
                                color = Color(0xFF333333),
                                fontSize = 12.sp, fontWeight = FontWeight.Normal)
                            Spacer(Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                "angelbracket",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(13.dp)
                            )

                        }

                        Spacer(Modifier.height(15.dp))


                        Row(Modifier.border(width = 1.dp, Color(0xFF333333), RoundedCornerShape(10.dp)).padding(start = 12.dp, 6.dp, bottom = 6.dp, end = 12.dp).height(35.dp).clickable(onClick = {
                            val intent2 = Intent(context, OfferPromotion::class.java)
                            context.startActivity(intent2)
                        }), verticalAlignment = Alignment.CenterVertically) {
                            Text("Promotion",
                                color = Color(0xFF333333),
                                fontSize = 12.sp, fontWeight = FontWeight.Normal)
                            Spacer(Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                "angelbracket",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(13.dp)
                            )

                        }

Spacer(Modifier.height(15.dp))

                        Row(Modifier.border(width = 1.dp, Color(0xFF333333), RoundedCornerShape(10.dp)).padding(start = 12.dp, 6.dp, bottom = 6.dp, end = 12.dp).height(35.dp).clickable(onClick = {
                            val intent2 = Intent(context, Offer::class.java)
                            context.startActivity(intent2)
                        }),verticalAlignment = Alignment.CenterVertically) {
                            Text("Offer",
                                color = Color(0xFF333333),
                                fontSize = 12.sp, fontWeight = FontWeight.Normal)
                            Spacer(Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                "angelbracket",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(13.dp)
                            )

                        }

                        Spacer(Modifier.height(15.dp))

                        Row(Modifier.border(width = 1.dp, Color(0xFF333333), RoundedCornerShape(10.dp)).padding(start = 12.dp, 6.dp, bottom = 6.dp, end = 12.dp).height(35.dp).clickable(onClick = {
                            val intent2 = Intent(context, Transaction::class.java)
                            context.startActivity(intent2)
                        }),verticalAlignment = Alignment.CenterVertically) {
                            Text("Transaction History",
                                color = Color(0xFF333333),
                                fontSize = 12.sp, fontWeight = FontWeight.Normal)
                            Spacer(Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                "angelbracket",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(13.dp)
                            )

                        }

                }}

Spacer(Modifier.height(30.dp))
            }
        }
    }

if (showcheckqr){

    checkqr(onDismiss = { showcheckqr = false },
      )

}

}

@Composable
fun checkqr(onDismiss: () -> Unit, vm: HomePageviewmodel = viewModel() ) {
    val qrResult by vm.qrCheckResult.collectAsState()
    var scanQRCodes by remember { mutableStateOf("") }
    var shotoast by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {onDismiss()}){
Box(Modifier.clip(RoundedCornerShape(10.dp)).wrapContentHeight().background(Color.White)) {

        Column {
Row( modifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 18.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically) {
    androidx.compose.material3.Text(
    text = "Check the QR/ID", fontWeight = FontWeight.Medium,
    modifier = Modifier
        .padding(end = 16.dp),
)
Spacer(modifier = Modifier.weight(1f))
    IconButton(onClick = { onDismiss() }, modifier = Modifier.clip(CircleShape).background(Color(0xFFEEF0FB))) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close",
            tint = Color(0xFF333333),
            modifier = Modifier.size(16.dp)
        )
    }
}

            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = "Product Id",
                modifier = Modifier
                    .padding(start = 18.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            androidx.compose.material3.OutlinedTextField(
                value = scanQRCodes?: "",
                onValueChange = { scanQRCodes = it},
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

Spacer(Modifier.height(13.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
            ) {

                OutlinedButton(

                    onClick = {
                       scanQRCodes =""
                    },
                    Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.Black),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent, // Transparent background
                        contentColor = Color.Black
                    )
                ) {
                    Text("Clean")
                }
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedButton(
                    onClick = {
                        vm.checkqrmv(scanQRCodes)
                        shotoast = true

                    },
                    Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.Black),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Submit")
                }



            }


            Spacer(Modifier.height(16.dp))



            if (shotoast){
                if (qrResult?.exists == true){
                    Text("Qr alredy scanned", Modifier.padding(10.dp))
                }
                if (qrResult?.exists == false){
                    Text("Qr not scanned", Modifier.padding(10.dp))
                }

                Spacer(Modifier.height(16.dp))

            }
        }
    }}

}


suspend fun downloadTransactionCsv(context: Context): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val response =HomePageRetro.api.downloadTransactions()
            if (response.isSuccessful) {
                val body: ResponseBody? = response.body()
                if (body != null) {
                    val fileName = "transactions_${System.currentTimeMillis()}.csv"
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDir, fileName)

                    saveFile(body.byteStream(), file)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

private fun saveFile(inputStream: InputStream, file: File) {
    FileOutputStream(file).use { output ->
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
        output.flush()
    }
}


@Composable
fun Cardview(title: String, value: Int, onClick: () -> Unit) {
    val formatter = DecimalFormat("#,###")
    val formattedValue = try {
        formatter.format(value.toLong())
    } catch (e: Exception) {
        value
    }

    Box(modifier = Modifier.clip(
        RoundedCornerShape(20.dp)
    ).background(brush = Brush.linearGradient(colors = listOf(
        Color(0xFFFDF9E1),
        Color(0xFFC4C9EA)
    ))).padding(start = 20.dp, end = 20.dp, bottom = 20.dp).fillMaxWidth().wrapContentHeight()){
        Column {

            Row(modifier = Modifier.padding(top = 20.dp, start = 8.dp)) {   Image(
                painter = painterResource(R.drawable.defaulticon),
                contentDescription = "icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
                Text(title, color = Color.Black, fontSize = 20.sp)

            }

            Row (modifier = Modifier.padding(top = 20.dp, start = 8.dp)){
                Text(formattedValue.toString(), color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 5.dp, start = 8.dp))
                Spacer(Modifier.weight(1f))

                Text("View Details", color = Color.Black, fontSize = 12.sp, modifier = Modifier.padding(end = 20.dp).clickable(onClick = onClick))

            }
          
     }

    }

}


@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}