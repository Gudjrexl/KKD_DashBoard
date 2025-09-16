package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.R
import com.jrexl.novanectorclient.modelview.WithdrawalModelView
import java.text.DecimalFormat

class IndTrans : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactno = intent.getStringExtra("contactno") ?: ""

        setContent{
            individualtrans(contactno)
        }
    }
}

@Composable
fun individualtrans(contactno: String, vm: WithdrawalModelView = viewModel()){
    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    LaunchedEffect(Unit) {
        vm.transactionhistorymodelfunc(contactno)
        vm.fetchCardInfo(contactno)

    }
    val transactions by vm.transactionsList.collectAsState()
    
var showupdatediologue by remember { mutableStateOf(false) }
    if (showupdatediologue){
        updatediog(
            onDismiss = { showupdatediologue = false },
            vm = vm,
            contactno
        )
    }
    val context = LocalContext.current
    var coin by remember { mutableStateOf(0) }
coin = vm.cardInfo?.coins ?: 0
    val formattedCoin = DecimalFormat("#,###").format(coin)

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFB3E5FC),
                Color.White
            )
        )
    )){
        Row(modifier = Modifier.statusBarsPadding().padding(start = 4.dp)) { IconButton(
            onClick = {
                (context as? Activity)?.finish() },
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(25.dp)
            ) }
            Text(text = "History", fontSize = 20.sp, modifier = Modifier.padding(start = 1.dp, top = 13.dp), color = Color.Black)
        }
        Text(
            text = formattedCoin,
            fontSize = 30.sp,
            color = Color.Black,
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Available Balance",
            fontSize = 20.sp,
            color = Color.Black,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                showupdatediologue = true
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )) {
                Text(
                    "Update Transaction",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn {
            items(transactions){
                    tra->  Column(modifier = Modifier
                .padding(8.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.paintimg),
                            contentDescription = "Paint",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = tra.itemexp, fontSize = 15.sp, color = Color.Black)
                        Spacer(modifier= Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.dollar),
                                contentDescription = "Coin",
                                modifier = Modifier.size(16.dp)  // Adjust size as needed
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = if (tra.typetransaction == "deduct") "-${tra.coins}" else "+${tra.coins}",
                                fontSize = 15.sp,
                                color = if (tra.typetransaction == "deduct") Color.Red else Color(0xFF008000),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }

                    }
                }}
            }
        }


    }
}


@Composable
fun updatediog(
    onDismiss: () -> Unit,
    vm: WithdrawalModelView,
    contactcno : String
) {
    var isAdd by remember { mutableStateOf(true) }
    var productId by remember { mutableStateOf("") }
    var coin by remember { mutableStateOf("") }

    val creditResult by vm.creditResult.collectAsState()
    val deductResult by vm.deductResult.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Update Transaction",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Toggle Add/Subtract
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { isAdd = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isAdd) Color.Black else Color.White,
                            contentColor = if (isAdd) Color.White else Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add ")
                    }

                    Spacer(Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { isAdd = false },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (!isAdd) Color.Black else Color.White,
                            contentColor = if (!isAdd) Color.White else Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Subtract ")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Product ID
                Text("Product Id", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = productId,
                    onValueChange = { productId = it },
                    placeholder = { Text("Enter Product id", fontSize = 15.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // default Material size
                    textStyle = TextStyle(fontSize = 15.sp),
                )


                Spacer(Modifier.height(12.dp))

                // Coin
                Text("Coin", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                        value = coin,
                onValueChange = { coin = it },
                placeholder = { Text("Enter Coin", fontSize = 15.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // default Material size
                textStyle = TextStyle(fontSize = 15.sp),
                )


                Spacer(Modifier.height(20.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            productId = ""
                            coin = ""
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).height(40.dp),
                    ) {
                        Text("Clean")
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (productId.isNotBlank() && coin.isNotBlank()) {
                                if (isAdd) {
                                    vm.credit(
                                        contactcno = contactcno,
                                        coins = coin.toInt(),
                                        scanQRCodes = productId,

                                    )
                                } else {
                                    vm.debit(
                                        contactcno = contactcno,
                                        coins = coin.toInt(),
                                        reason = productId
                                    )
                                }
                                vm.fetchCardInfo(contactcno)

                            }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Show result message
                val message = creditResult ?: deductResult
                if (message != null) {
                    Text(
                        text = message,
                        color = Color.Blue,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}




