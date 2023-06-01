package com.example.messagesender

import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            onClick = { sendMessage(phoneNumberState.split("\n"), messageState) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Send Message")
        }
    }
}

fun sendMessage(phoneNumbers: List<String>, message: String) {
    val smsManager = SmsManager.getDefault()
    for (number in phoneNumbers) {
        smsManager.sendTextMessage(number.trim(), null, message, null, null)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MessageSenderTheme {
        PhoneSenderScreen()
    }
}

