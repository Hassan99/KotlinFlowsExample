package com.example.kotlinflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinflows.ui.theme.KotlinFlowsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinFlowsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

var countDownText = MutableStateFlow<Int>(0)
var _countDownText = countDownText.asStateFlow()

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    Column {


        TextField(
            value = text, onValueChange = { newText ->
                text = newText
            },

            label = { Text("Enter initial Value") },

            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )
        Text(
            text = "Hello ${_countDownText.collectAsState().value}", modifier = modifier
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                countdown(text.toInt())
                    .stateIn(CoroutineScope(Dispatchers.Main))
                    .collectLatest { item ->
                        countDownText.value = item
                    }
            }
        }) {
            Text(text = "Start Countdown")
        }
    }
}

suspend fun countdown(from: Int) = flow {
    for (i in from downTo 0) {
        emit(i)
        delay(1000)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinFlowsTheme {
        Greeting("Android")
    }
}