package com.jrexl.novanectorclient.secondscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jrexl.novanectorclient.dataclass.ProfileDataClass
import com.jrexl.novanectorclient.modelview.Kycmodelview
import com.jrexl.novanectorclient.modelview.UserprofileViewmodel
import com.jrexl.novanectorclient.objectapi.Urlobject

class Completeprofile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactno = intent.getStringExtra("contactno") ?: ""

        setContent{
            Completeprof(contactno)
        }
    }
}
@Composable
fun Completeprof(contactno: String, vm: UserprofileViewmodel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        vm.profiledatagetviewmode(contactno)
    }
    Column(modifier = Modifier.verticalScroll(scrollState) .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFB3E5FC),
                Color.White
            )
        )
    ).padding(start = 16.dp, end=16.dp)) {
         Row(modifier = Modifier.statusBarsPadding().padding(top = 8.dp)){

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
            Text(text = "User Profile", fontSize = 20.sp, color = Color.Black)
             Spacer(Modifier.weight(1f))
             Text(
                 text = "History",
             fontSize = 16.sp,
             color = Color.Blue,
             modifier = Modifier.clickable {
                 val intent = Intent(context, IndTrans::class.java)
                 intent.putExtra("contactno", contactno)
                 context.startActivity(intent)
             }
             )


         }
        Spacer(modifier = Modifier.height(8.dp))

        Profileheader(pf = vm.profilevm)
        Spacer(modifier = Modifier.height(3.dp))
        Spacer(modifier = Modifier.height(3.dp))
        PersonalInfo(pf= vm.profilevm)
        Spacer(modifier = Modifier.height(3.dp))
        Kyc(pf= vm.profilevm, contactno)
        Spacer(modifier = Modifier.height(3.dp))
        Documents(pf= vm.profilevm)
        Spacer(modifier = Modifier.height(100.dp))

    }
}

@Composable
fun Documents(pf: ProfileDataClass?) {


    val context = LocalContext.current
    Box(modifier = Modifier.padding(8.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0x4FD5DDF4).copy(alpha = 0.3f))){
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween){
            Text(text = "Bank details", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)


            HorizontalDivider(
                modifier = Modifier.padding(vertical = 3.dp),
                thickness = 1.dp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Account Number", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.BankAccount ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Account Holder", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.BankHolderName ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Bank Name", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.BankName ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "IFSC Code", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.ifsc ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }

        }
    }}

@Composable
fun Kyc(pf: ProfileDataClass?, contactno: String) {
val context = LocalContext.current

    Box(modifier = Modifier.clickable(onClick = {
        val intent = Intent(context, kycdetailsindv::class.java)
        intent.putExtra("contactno", contactno)
        intent.putExtra("hide","hide")
        context.startActivity(intent)
    }).padding(8.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0x4FD5DDF4).copy(alpha = 0.3f))){
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween){
            Text(text = "KYC", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black,)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Aadhar Card", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.AadharCardVerify ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Pan Card", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.PanCardVerify ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }

        }
    }

}


@Composable
fun PersonalInfo(pf: ProfileDataClass?) {
    val context = LocalContext.current
    Box(modifier = Modifier.padding(8.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0x4FD5DDF4).copy(alpha = 0.3f))){
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Personal Details", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Contact Number", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.contactno ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Email id", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.gmailid ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Date of Birth", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.dob ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Permanent Address", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.address ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Pin Code", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                pf?.Pincode?.let {
                    Text(
                        text = it.toString(),
                        fontSize = 12.sp,
                        color = Color.Black,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "State", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.State ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Country", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(text = pf?.Country ?:"" , fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
        }

    }}



@Composable
fun Profileheader(pf: ProfileDataClass?) {


    Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.Top) {
        val context = LocalContext.current
        val imageUrl = "${Urlobject.BASE_URL.trimEnd('/')}/profilepic/${pf?.ProfilePic.orEmpty()}"


        Log.d("ProfilePicURL", "Image URL = $imageUrl")
        AsyncImage(
            ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            modifier = Modifier.padding(8.dp).clip(CircleShape).size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.CenterVertically)){
            Text(text = pf?.uname ?:"" , fontSize = 20.sp, fontWeight = FontWeight.Bold )
//            Text(text = pf?.designation ?:"" , fontSize = 12.sp, color = Color.Gray)


        }
    }


}
