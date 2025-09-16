package com.jrexl.novanectorclient


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.modelview.loginclientmodelview
import com.jrexl.novanectorclient.objectapi.Datamanger

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginPage()
        }
    }
}

@Composable
fun LoginPage() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
val context = LocalContext.current
    var userid by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val vm: loginclientmodelview = viewModel()
    val loginResult by vm.loginResult.collectAsState()

    Column( modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFADD8E6), Color.White)
            )
        )) {








        Text("Welcome Back!", fontSize = 30.sp, modifier = Modifier.statusBarsPadding().padding(start= 16.dp, top=(10.dp)))
        Text("Log in to continue using your account.", fontSize = 20.sp, modifier = Modifier.padding(16.dp))



        Text(text = "Enter user id", fontWeight = FontWeight.Bold, modifier = Modifier
            .padding(start = 18.dp),)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = userid,
            onValueChange = { userid = it },
            placeholder = { Text("Enter user id", fontSize = 10.sp,lineHeight = 14.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(start = 16.dp, end = 10.dp)

            ,

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "Enter password", fontWeight = FontWeight.Bold, modifier = Modifier
            .padding(start = 18.dp),)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            placeholder = { Text("Enter password", fontSize = 10.sp,  lineHeight = 14.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .height(48.dp)

            ,

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(14.dp))





Spacer(modifier = Modifier.height(350.dp))
        Button(onClick = {
            isLoading = true
            vm.login(userid, pass) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .height(50.dp), enabled = !isLoading, ){Text("Login", fontSize = 20.sp)}
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        LaunchedEffect(loginResult) {
            if (loginResult == "true") {
                isLoading = false
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                Datamanger.saveClient(context, userid)
                context.startActivity(Intent(context, Homepage::class.java))
                (context as Activity).finish()
            } else if (loginResult == "false") {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                isLoading = false
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as Activity).finish()
            }
        }
    }


}

@Composable
@Preview(showBackground = true)
fun LoginPagePreview() {
    LoginPage()
}