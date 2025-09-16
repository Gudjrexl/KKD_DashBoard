package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.modelview.WithdrawalModelView
import java.util.Calendar
import kotlin.inc
import kotlin.rem

class Transaction : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Transactionhistory()
        }
    }
}

@Composable
fun Transactionhistory(vm: WithdrawalModelView = viewModel()) {
    @Suppress("DEPRECATION") val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    val context = LocalContext.current
    val activity = context as? Activity
    LaunchedEffect(Unit) {
        Log.d("TransactionUI", "Initial LaunchedEffect → calling vm.trasactionall() with no filters")
        vm.trasactionall()
    }

    val hometrans by vm.alltransaction.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    var startdate by remember { mutableStateOf("") }
    var enddate by remember { mutableStateOf("") }




    if (showDialog) {
        showdaterange(
            startdate = startdate,
            enddate = enddate,
            onStartDateChange = { startdate = it },
            onEndDateChange = { enddate = it },
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false

                if (startdate.isNotBlank() && enddate.isNotBlank()) {
                    Log.d("TransactionFilter", "Confirm clicked → start=$startdate , end=$enddate")
                    vm.trasactionall(startdate, enddate)
                } else {
                    Log.d("TransactionFilter", "Confirm clicked → No dates provided, fetching all transactions")
                    vm.trasactionall()
                }
            }


        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB3E5FC),
                        Color.White
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp)
        ) {
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
            Text(text = "Transaction History", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Filter",
                fontSize = 20.sp,
                color = Color.Blue,
                modifier = Modifier.clickable(onClick = {
                    showDialog = true
                })
            )
        }

        Spacer(Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)
        ) {
            hometrans?.let { profileList ->
                items(
                    items = profileList,
                    key = { it.requestid ?: it.userid } // pick a guaranteed unique field
                ) { profile ->
                    Withdrawalcards(
                        primg = profile.username?.firstOrNull()?.uppercaseChar()?.toString() ?: "N",
                        username = profile.username ?: "name",
                        contact = profile.contactcno ?: "phone",
                        coin = profile.coins ?: 0,
                        requestid = profile.requestid ?: "00",
                        date = profile.date ?: "01/12/1990",
                        userid = profile.userid ?: "0000",
                        itemexp = profile.itemexp ?: "",
                        typetransaction = profile.typetransaction?:"",
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showdaterange(
    startdate: String,
    enddate: String,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier.wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Select date", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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

                Text("Start Date", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Normal)
                Spacer(modifier = Modifier.height(10.dp))

                val context = LocalContext.current
                OutlinedTextField(
                    value = startdate,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select start Date") },
                    trailingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            android.app.DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    onStartDateChange("$day/${month + 1}/$year")
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                        }
                    }
                )
                Spacer(Modifier.height(18.dp))

                Text("End Date", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Normal)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = enddate,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select end Date") },
                    trailingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            android.app.DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    onEndDateChange("$day/${month + 1}/$year")
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(Modifier.padding(bottom = 16.dp)) {
                    Button(
                        onClick = {
                            onStartDateChange("")
                            onEndDateChange("")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.Black),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text("Clean")
                    }
                    Spacer(Modifier.width(15.dp))
                    Button(
                        onClick = { onConfirm() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun Withdrawalcards(
    primg: String,
    username: String,
    contact: String,
    coin: Int,
    requestid: String,
    itemexp: String,
    typetransaction: String,
    date: String,
    userid: String
) {

    var showdetailsind by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .wrapContentHeight()
            .clickable{

                showdetailsind = !showdetailsind


            }
            .background(Color.White)
    ) {
        Column {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = primg,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .background(Color.Gray)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
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
                    text = requestid,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.MonetizationOn,
                    contentDescription = "Back",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    coin.toString(),
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.width(6.dp))
        }
            if (showdetailsind){
                Column(modifier = Modifier.padding(top = 8.dp, end = 16.dp, start = 16.dp, bottom = 5.dp)) {
                    Row {
                        Text("UserId :")
                        Spacer(Modifier.weight(1f))
                        Text(userid)

                    }
                    Row {
                        Text("Date :")
                        Spacer(Modifier.weight(1f))
                        Text(date)

                    }
                    Row {
                        Text("Phone :")
                        Spacer(Modifier.weight(1f))
                        Text(contact)

                    }
                    Row {
                        Text("Reason :")
                        Spacer(Modifier.weight(1f))
                        Text(itemexp)

                    }
                    Row {
                        Text("Transaction Type :")
                        Spacer(Modifier.weight(1f))
                        Text(typetransaction)

                    }
                }
                Spacer(Modifier.height(8.dp))

            }
    }

    }
}
