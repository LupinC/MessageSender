package com.example.messagesender

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.messagesender.ui.theme.MessageSenderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageSenderTheme {
                // A surface container using the 'background' color from the theme
                Surface() {
                    PhoneSenderScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneSenderScreen() {
    var phoneNumberState by remember { mutableStateOf("") }
    var messageState by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(modifier = Modifier.padding()) {
        Text(text = "Enter phone numbers (one per line):")
        TextField(
            value = phoneNumberState,
            onValueChange = { phoneNumberState = it },
            maxLines = Int.MAX_VALUE,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Text(text = "Enter message:")
        TextField(
            value = messageState,
            onValueChange = { messageState = it },
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Pick Image")
        }

        Button(
            onClick = { sendMessage(context, phoneNumberState.split("\n"), messageState, imageUri) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Send Message")
        }
    }
}

fun sendMessage(context: Context, phoneNumbers: List<String>, message: String, imageUri: Uri?) {
    val smsManager = SmsManager.getDefault()
    for (number in phoneNumbers) {
        val parts = smsManager.divideMessage(message)
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra("address", number.trim())
        sendIntent.putExtra("sms_body", message)
        if (imageUri != null) {
            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            sendIntent.type = context.contentResolver.getType(imageUri)
        }
        context.startActivity(sendIntent)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MessageSenderTheme {
        PhoneSenderScreen()
    }
}
